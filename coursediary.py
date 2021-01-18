from flask import *
import pymysql
import os
path=r"static/uploadfile"
syllabuspath=r"static/syllabus"
from src.ocr_main import *
from werkzeug.utils import secure_filename
obj=Flask(__name__)
con=pymysql.connect(host='localhost',port=3306,user='root',password='',db='course_diary')
cmd=con.cursor()
obj.secret_key='abc'

@obj.route('/')
def index():
    return render_template('index.html')

@obj.route('/login')
def login():
    return render_template('admin_login.html')

@obj.route('/log',methods=['post'])
def log():
    uname=request.form['username']
    password=request.form['password']
    cmd.execute("SELECT * FROM login WHERE `Username`='"+uname+"' AND `Password`='"+password+"'")
    s=cmd.fetchone()
    if s is None:
        return '''<script> alert('Invalid username and password'); window.location='/'</script>'''
    else:
        return render_template('adminpage.html')

@obj.route('/admin')
def admin():
    return render_template('adminpage.html')

@obj.route('/teacher')
def teacher():
    return render_template('registerteacher.html')

@obj.route('/tregister',methods=['post'])
def tregister():
    firstname=request.form['fname']
    lastname=request.form['lname']
    age= request.form['age']
    gender = request.form['radio']
    phno= request.form['textfield4']
    uname=request.form['textfield5']
    password=request.form['textfield6']
    cmd.execute("insert into login values(null,'"+uname+"','"+password+"','teacher')" )
    id=con.insert_id()
    cmd.execute("insert into teacher values('"+str(id)+"','"+firstname+"','"+lastname+"','"+age+"','"+gender+"','"+phno+"')")
    con.commit()
    return '''<script> alert('Registration successful'); window.location='/admin'</script>'''

@obj.route('/manageteacher')
def manageteacher():
    cmd.execute("select * from teacher ")
    s=cmd.fetchall()
    return render_template('manageteacher.html',value=s)

@obj.route('/tdelete')
def tdelete():
    id=request.args.get('id')
    cmd.execute("Delete from teacher where Teacher_id='"+id+"'")
    cmd.execute("Delete from login where id='"+id+"'")
    con.commit()
    return '''<script> alert('Deleted successfully'); window.location='/manageteacher'</script>'''

@obj.route('/t_edit')
def t_edit():
    id = request.args.get('id')
    session['t_id']=id
    cmd.execute("select * from teacher where Teacher_id='"+id+"'")
    result=cmd.fetchone()
    return render_template('editteacher.html',value=result)

@obj.route('/tregister_update',methods=['post'])
def tregister_update():
    firstname=request.form['fname']
    lastname=request.form['lname']
    age= request.form['age']
    gender = request.form['radio']
    phno= request.form['textfield4']
    t_id=session['t_id']
    cmd.execute("update teacher set First_Name='"+firstname+"',Last_Name='"+lastname+"',Age='"+age+"',Gender='"+gender+"',Phone_no='"+phno+"' where Teacher_id='"+t_id+"'")
    con.commit()
    return '''<script> alert('Updated successfully'); window.location='/manageteacher'</script>'''

@obj.route('/assignedsubject')
def assignedsubject():
    cmd.execute("SELECT teacher.`First_Name`,`Last_Name`,subject_registration.`Sem`,`Course_Code`,`Course_Name` FROM subject_assign JOIN teacher ON teacher.Teacher_id=subject_assign.`Faculty_id` JOIN subject_registration ON subject_registration.subject_id=subject_assign.subject_id")
    s=cmd.fetchall()
    return render_template('assignedsubject.html',value=s)

@obj.route('/assignedsubjectview',methods=['post'])
def assignedsubjectview():
    subject_id = request.form['select2']
    faculty_id = request.form['select3']
    cmd.execute("insert into subject_assign values(null,'"+subject_id+"','"+faculty_id+"')")
    con.commit()
    return '''<script> alert('Assigned successful'); window.location='/admin'</script>'''

@obj.route('/subject_assign')
def subject_assign():
    cmd.execute("select * from teacher ")
    q=cmd.fetchall()
    return render_template('subject_assign.html',value=q)

@obj.route('/subject_registration')
def subject_registration():
    return render_template('subject_registration.html')

@obj.route('/subjectregister',methods=['post'])
def subjectregister():
    sem=request.form['select']
    coursecode=request.form['textfield']
    coursename=request.form['textfield2']
    cmd.execute("insert into  subject_registration values(null,'"+sem+"','"+coursecode+"','"+coursename+"')" )
    con.commit()
    return '''<script> alert('Registration successful'); window.location='/admin'</script>'''

@obj.route('/subject_view')
def subject_view():
    cmd.execute("select * from subject_registration ")
    s = cmd.fetchall()
    return render_template('subject_view.html',value=s)

@obj.route('/regstudent')
def regstudent():
    return render_template('regstudent.html')

@obj.route('/sregister',methods=['post'])
def sregister():
    registernumber=request.form['rgno']
    firstname=request.form['fname']
    lastname=request.form['lname']
    rollnumber=request.form['rno']
    gender=request.form['radio']
    semester=request.form['semester']
    phno=request.form['phoneno']
    cmd.execute("insert into student values(null,'"+registernumber+"','"+firstname+"','"+lastname+"','"+rollnumber+"','"+gender+"','"+semester+"','"+phno+"')")
    con.commit()
    return '''<script> alert('Registration successful'); window.location='/admin'</script>'''

@obj.route('/managestudent',methods=['post','get'])
def managestudent():
    cmd.execute("select * from student ")
    s=cmd.fetchall()
    return render_template('managestudent.html',value=s)

@obj.route('/sdelete')
def sdelete():
    id=request.args.get('id')
    cmd.execute("Delete from student where Student_id='"+id+"'")
    con.commit()
    return '''<script> alert('Deleted successfully'); window.location='/managestudent'</script>'''

@obj.route('/s_edit')
def s_edit():
    id = request.args.get('id')
    session['s_id']=id
    cmd.execute("select * from student where Student_id='"+id+"'")
    result=cmd.fetchone()
    return render_template('editstudent.html',value=result)

@obj.route('/sregister_update',methods=['post'])
def sregister_update():
    registernumber = request.form['rgno']
    firstname = request.form['fname']
    lastname = request.form['lname']
    rollnumber = request.form['rno']
    gender = request.form['radio']
    semester = request.form['semester']
    phno = request.form['phoneno']
    s_id=session['s_id']
    cmd.execute("update student set Register_Number='"+registernumber+"',First_Name='"+firstname+"',Last_Name='"+lastname+"',Roll_No='"+rollnumber+"',Gender='"+gender+"',Sem='"+semester+"',Phone_Number='"+phno+"' where Student_id='"+s_id+"'")
    con.commit()
    return '''<script> alert('Updated successfully'); window.location='/managestudent'</script>'''

@obj.route('/uploadphoto')
def uploadphoto():
    return render_template('uploadphoto.html')

@obj.route('/uploadfile',methods=['post'])
def uploadfile():
    file=request.files['photo']
    fn=secure_filename(file.filename)
    file.save(os.path.join(path,fn))
    res=main(os.path.join(path,fn))
    return res

@obj.route('/uploadsyllabus')
def uploadsyllabus():
    cmd.execute("SELECT * FROM subject_registration")
    t = cmd.fetchall()
    return render_template('uploadsyllabus.html',values=t)

@obj.route('/syllabus',methods=['post'])
def syllabus():
    course=request.form['select']
    sem=request.form['sem']
    syllabus=request.files['syllabus']
    fn = secure_filename(syllabus.filename)
    syllabus.save(os.path.join(syllabuspath, fn))
    cmd.execute("insert into syllabus values(null,'"+course+"','"+sem+"','"+fn+"')")
    con.commit()
    return '''<script> alert('Uploaded successfully'); window.location='/admin'</script>'''

@obj.route('/vieweditsyllabus')
def vieweditsyllabus():
    return render_template('vieweditsyllabus.html')

@obj.route('/update_syllabus',methods=['post'])
def update_syllabus():
    try:
        course=request.form['select']
        sem=request.form['sem']
        syllabus=request.files['syllabus']
        fn = secure_filename(syllabus.filename)
        syllabus.save(os.path.join(syllabuspath, fn))
        syl=session['sy_id']
        cmd.execute("update syllabus set(Course='"+course+"',Sem='"+sem+"',Syllabus='"+fn+"' where Syllabs_id='"+syl+"')")
        con.commit()
        return '''<script> alert('Updated successfully'); window.location='/vieweditsyllabus'</script>'''
    except Exception as e:
        course = request.form['select']
        sem = request.form['sem']
        syl = session['sy_id']
        cmd.execute("update syllabus set(Course='"+course+"',Sem='"+sem+"',Syllabus='"+fn+"' where Syllabs_id='"+syl+"')")
        con.commit()
        return '''<script> alert('Updated successfully'); window.location='/vieweditsyllabus'</script>'''

@obj.route('/syllabus_edit')
def syllabus_edit():
    id = request.args.get('sy_id')
    session['sy_id']=id
    cmd.execute("select * from syllabus where Syllabus_id='"+id+"'")
    result=cmd.fetchone()
    return render_template('editstudent.html',value=result)

@obj.route('/attendance')
def attendance():
    # cmd.execute("select * from syllabus")
    # result=cmd.fetchall()

    return render_template('attendanceview.html')

@obj.route('/attendsearch',methods=['get','post'])
def attendsearch():
    subject= request.form['course']
    cmd.execute(
        "SELECT `student`.`first_name`,`student`.`last_name`,COUNT(*),SUM(`attendance`),(SUM(`attendance`)/COUNT(*))*100 FROM `attendance` JOIN `student` ON `student`.`roll_no`=`attendance`.`register_number` WHERE `attendance`.`Subject`='"+subject+"' GROUP BY `attendance`.register_number")
    t = cmd.fetchall()
    return render_template('attendanceview.html', val=t)


@obj.route('/semsearch',methods=['get','post'])
def semsearch():
    sem= request.form['sem']
    cmd.execute("select * from subject_registration where sem='"+sem+"'")
    s=cmd.fetchall()
    lis=[0,'select']
    for r in s:
        lis.append(r[0])
        lis.append(r[2])
    print(lis)
    resp = make_response(jsonify(lis))
    resp.status_code = 200
    resp.headers['Access-Control-Allow-Origin'] = '*'
    return resp

@obj.route('/logout')
def logout():
    return render_template('admin_login.html')































if(__name__=='__main__'):
    obj.run(debug=True)

