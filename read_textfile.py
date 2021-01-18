def plan(fname):
    f = open(fname, "r")
    l1=[]
    l2=[]
    out=[]
    line = f.readlines()
    for i in line:
        sub=i.split("#")
        l1.append(sub[0])
        l2.append(int(sub[1].strip()))


        #if line is empty, you are done with all lines in the file


    #close file

    f.close
    tot=sum(l2)
    print(sum(l2))
    print(l2)



    for ii in l2:
        out.append(ii/tot*50)
    row_headers=["Topics","Hours"]
    json_data=[]
    for i in range(len(l1)):
        rows=[]
        rows.append(l1[i])
        rows.append(out[i])
        json_data.append(dict(zip(row_headers, rows)))



    print(len(l1))


    print(json_data)
    return json_data