from textblob import TextBlob
import csv, os, glob, json
files = []
filecount = 0
os.chdir("D:\## THESIS\ThesisData")
for location in glob.glob("*.csv"):
    filecount += 1
    posCount =0
    negCount = 0
    neutCount = 0
    vneutCount = 0
    revCount = 0
    subjectivityTot =0
    posGT = 0   
    posLT = 0
    negGT = 0
    negLT = 0
    neuGT = 0
    neuLT = 0
    with open(location,'r',encoding="utf8") as csvfile:
        reviews = csv.reader(csvfile,delimiter=",")
        for row in reviews:
            review = row[4].encode(encoding='utf-8')
            decoded = TextBlob(review.decode('unicode-escape'))
            if(decoded.sentiment[0] > 0.0 and decoded.sentiment[1] >0.0):
                posCount += 1
                revCount += 1
                subjectivityTot += decoded.sentiment[1]/1
            elif(decoded.sentiment[0] < 0.0 and decoded.sentiment[1] > 0.0):
                negCount += 1
                revCount += 1
                subjectivityTot += decoded.sentiment[1]/1
            elif(decoded.sentiment[0] == 0.0 and decoded.sentiment[1] >= 0.0):
                neutCount += 1
                revCount += 1
                subjectivityTot += decoded.sentiment[1]/1
    #average subjectivity score
    average = subjectivityTot/revCount
    with open(location,'r',encoding="utf8") as csvfile:
        reviews = csv.reader(csvfile,delimiter=",")
        for row in reviews:
            review = row[4].encode(encoding='utf-8')
            decoded = TextBlob(review.decode('unicode-escape'))
            if(decoded.sentiment[0] > 0.0 and decoded.sentiment[1] > average): #positive sentiments & subjectivity is greater than average
                posGT += 1
            elif(decoded.sentiment[0] > 0.0 and decoded.sentiment[1] < average): #positive sentiments & subjectivity is less than average
                posLT += 1
            elif(decoded.sentiment[0] < 0.0 and decoded.sentiment[1] > average): #negative sentiments & subjectivity is greater than average
                negGT += 1
            elif(decoded.sentiment[0] < 0.0 and decoded.sentiment[1] < average): #negative sentiments & subjectivity is less than average
                negLT += 1
            elif(decoded.sentiment[0] == 0.0 and decoded.sentiment[1] > average): #neutral sentiments & subjectivity is greater than average
                neuGT += 1
            elif(decoded.sentiment[0] == 0.0 and decoded.sentiment[1] < average): #neutral sentiments & subjectivity is less than average
                neuLT += 1

    print('File: ' + location[0:location.find('.')])
    print('Review Count: ' + str(revCount))
    print('Subjectivity Score Average: ' +str(average))
    print('Positive Sentiments: ' +str(posCount))
    print('positive & subjectivity is greater than average: ' +str(posGT))
    print('positive & subjectivity is less than average: ' +str(posLT))
    print('Negative Sentiments: ' +str(negCount))
    print('negative & subjectivity is greater than average: ' +str(negGT))
    print('negative & subjectivity is less than average: ' +str(negLT))
    print('Neutral Sentiments: ' +str(neutCount))
    print('neutral & subjectivity is greater than average: ' +str(neuGT))
    print('neutral & subjectivity is less than average: ' +str(neuLT))
    print('----------')

    file = {}
    file['id'] = filecount
    file['Name'] = location[0:location.find('.')]
    file['ReviewCount'] = revCount
    file['SubjectivityScoreAverage'] = average
    file['Positive'] = posCount
    file['PositiveGTAvg'] = posGT
    file['PositiveLTAvg'] = posLT
    file['Negative'] = negCount
    file['NegativeGTAvg'] = negGT
    file['NegativeLTAvg'] = negLT
    file['Neutral'] = neutCount
    file['NeutralGTAvg'] = neuGT
    file['NeutralLTAvg'] = neuLT
    
    files.append(file)
    list = json.dumps(files)
    print("Adding "+location+" to JSON Array")
    print('----------')
with open('testresults.json', 'w') as outfile:  
    json.dump(files, outfile)
