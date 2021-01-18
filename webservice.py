from flask import *
import pymysql
import os
uploads=r"static/uploadfile"
from src.ocr_main import *
from src.read_textfile import *
from werkzeug.utils import secure_filename


obj=Flask(__name__)
con=pymysql.connect(host='localhost',port=3306,user='root',password='',db='course_diary')
cmd=con.cursor()

@obj.route('/login',methods=['POST'])
def login():
    try:
        uname=request.form['uname']
        passwd=request.form['password']
        cmd.execute("select * from login where username='"+uname+"' and password='"+passwd+"' and usertype='teacher'")
        s=cmd.fetchone()
        print(s)
        if s is  not None:
            id=s[0]
            print(id)
            return jsonify({'task':str(id)})
        else:
            return jsonify({'task':"error"})
    except Exception as e:
            print(str(e))
            return jsonify({'task':"error"})

@obj.route('/markattendance',methods=['POST'])
def mark_attendance():
    try:

        # regno = request.form['regno']
        subj = request.form['subj']
        fil = request.files['files']
        fn = secure_filename(fil.filename)
        fil.save(os.path.join(uploads, fn))
        # colorImage = Image.open(os.path.join(uploads, fn))
        #
        # transposed = colorImage.transpose(Image.ROTATE_45)
        #
        # transposed.save(os.path.join(uploads, fn))

        res = main(os.path.join(uploads, fn))
        val = res.split('D')
        atte = val[0].split(',')

        date=val[1]
        for i in atte:
            cmd.execute("insert into attendance values(NULL,'"+str(i)+"','"+subj+"','"+date+"',0)")
            con.commit()
        cmd.execute("SELECT `student`.`Roll_No` FROM `student` WHERE `student`.roll_no NOT IN( SELECT `attendance`.`Register_number` FROM `attendance` WHERE `Subject`='"+subj+"' AND `Date`='"+date+"')")
        reslts=cmd.fetchall()
        for ii in reslts:
            cmd.execute("insert into attendance values(NULL,'" + str(ii[0]) + "','" + subj + "','" + date + "',1)")
            con.commit()

        return jsonify({'task': "success"})

    except Exception as e:
        return jsonify({'task': "invalid"})

@obj.route('/viewsubject',methods=['POST'])
def viewsubject():
    teacher_id=request.form['teacher_id']
    cmd.execute("SELECT * FROM `subject_registration` JOIN `subject_assign` ON `subject_registration`.Subject_id = `subject_assign`.Subject_id WHERE `subject_assign`.Faculty_id = '"+teacher_id+"'")
    s = cmd.fetchall();

    print(s)
    row_headers = [x[0] for x in cmd.description]
    json_data = []
    for result in s:
        json_data.append(dict(zip(row_headers, result)))
    return jsonify(json_data)

@obj.route('/Subjects',methods=['POST'])
def Subjects():
        sem=request.form['sem']
        cmd.execute("SELECT * FROM `subject_registration` where Sem = '"+sem+"'")
        s = cmd.fetchall()

        print(s)
        row_headers = [x[0] for x in cmd.description]
        json_data = []
        for result in s:
            json_data.append(dict(zip(row_headers, result)))
        return jsonify(json_data)


@obj.route('/uploadsyllabus',methods=['POST'])
def uploadsyllabus():
    subject_id=request.form['subject_id']
    teacher_id=request.form['teacher_id']
    syllabus=request.files['files']
    fn = secure_filename(syllabus.filename)
    syllabus.save(os.path.join(uploads, fn))
    cmd.execute("insert into syllabus values(NULL,'"+subject_id+"','"+teacher_id+"','"+fn+"')")
    con.commit()
    return jsonify({'task': "success"})



@obj.route('/viewsyllabus',methods=['POST'])
def viewsyllabus():
    
    sem=request.form['sem']

    cmd.execute("SELECT `subject_registration`.*, `syllabus`.`Syllabus` FROM `subject_registration` JOIN `syllabus` ON `syllabus`.`Subject_id`=`subject_registration`.`Subject_id`  WHERE `subject_registration`.`Sem`='"+sem+"'")
    s = cmd.fetchall()

    print(s)
    row_headers = [x[0] for x in cmd.description]
    json_data = []
    for result in s:
        json_data.append(dict(zip(row_headers, result)))
    return jsonify(json_data)

@obj.route('/viewattendance',methods=['POST'])
def viewattendance():
    month=request.form['month']
    cmd.execute("SELECT `student`.`first_name`,`student`.`last_name`,`student`.`Register_number`,convert(((SUM(`attendance`)/COUNT(*))*100),char) AS attendance FROM `attendance` JOIN `student` ON `student`.`roll_no`=`attendance`.`register_number`  WHERE MONTH(`attendance`.`Date`)='"+month+"' GROUP BY `attendance`.register_number")
    s = cmd.fetchall()

    print(s)
    row_headers = [x[0] for x in cmd.description]
    json_data = []
    for result in s:
        json_data.append(dict(zip(row_headers, result)))
    return jsonify(json_data)
@obj.route('/courseplan',methods=['POST'])
def courseplan():
    syllabus=request.form['syllabus']
    output=plan(uploads+"/"+syllabus)
    return jsonify(output)


if(__name__=="__main__"):
    obj.run(host='0.0.0.0',port=5000)