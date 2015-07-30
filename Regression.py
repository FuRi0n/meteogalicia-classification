#!/usr/bin/python
# -*- coding: utf-8 -*-
import numpy as np
from sklearn import linear_model
from sklearn import cross_validation
from sklearn.externals import joblib
import sys
import sqlite3

if len(sys.argv)>1:
    temperaturaMS = []
    temperaturaMGMin = []
    temperaturaMGMax = []
    if sys.argv[1].endswith("txt"):
        f = open(sys.argv[1], 'r')
        for line in f:
            variables = line.split(';')
            temperaturaMS.append(sorted([int(i) for i in variables[5].split(',')]))
            temperaturaMGMin.append(int(variables[8].split(',')[0]))
            temperaturaMGMax.append(int(variables[8].rstrip().split(',')[1]))
        f.close()
    elif sys.argv[1].endswith("db"):
        conn = sqlite3.connect(sys.argv[1])
        c = conn.cursor()
        for row in c.execute("SELECT * FROM METEO WHERE ID_MG = " + sys.argv[2]):
            temperaturaMS.append(sorted([int(i) for i in row[5].split(',')]))
            temperaturaMGMin.append(int(row[8].split(',')[0]))
            temperaturaMGMax.append(int(row[8].split(',')[1]))
        conn.close()
    else:
        print("Error: Extensión del fichero/DB no válido.")
else:
    print("Error: Input file missing.")

#temperaturaMSTrain, temperaturaMSTest, temperaturaMGMinTrain, temperaturaMGMinTest, temperaturaMGMaxTrain, temperaturaMGMaxTest = cross_validation.train_test_split(
#    temperaturaMS, temperaturaMGMin, temperaturaMGMax, test_size=0.2, random_state=0)

clf = linear_model.LinearRegression()
#clf.fit(temperaturaMSTrain, temperaturaMGMinTrain)

#print ('Min Temperature')
#print 'Coefficients: \n', clf.coef_
#print("Residual sum of squares: %.2f" % np.mean((clf.predict(temperaturaMSTest) - temperaturaMGMinTest) ** 2))
#print('Variance score: %.2f' % clf.score(temperaturaMSTest, temperaturaMGMinTest))
scores = cross_validation.cross_val_score(clf, temperaturaMS, temperaturaMGMin, cv=10)
#print("Accuracy: %0.4f (+/- %0.2f)" % (scores.mean(), scores.std() * 2))
print(scores.mean())

# Save model
#clf.fit(temperaturaMS, temperaturaMGMin)
#joblib.dump(clf,"TemperaturaMin.regresion")

clf = linear_model.LinearRegression()
#clf.fit(temperaturaMSTrain, temperaturaMGMaxTrain)

#print ('Max Temperature')
#print 'Coefficients: \n', clf.coef_
#print("Residual sum of squares: %.2f" % np.mean((clf.predict(temperaturaMSTest) - temperaturaMGMaxTest) ** 2))
#print('Variance score: %.2f' % clf.score(temperaturaMSTest, temperaturaMGMaxTest))
scores = cross_validation.cross_val_score(clf, temperaturaMS, temperaturaMGMax, cv=10)
#print("Accuracy: %0.4f (+/- %0.2f)" % (scores.mean(), scores.std() * 2))
print(scores.mean())

# Save model
#clf.fit(temperaturaMS, temperaturaMGMax)
#joblib.dump(clf,"TemperaturaMax.regresion")


# print '\nRidge Regression:\n'
#
# for i in [1, 0.1, 0.01]:
#     clf = linear_model.Ridge(alpha=i)
#     #clf.fit(temperaturaMSTrain, temperaturaMGMinTrain)
#
#     print 'Min Temperature'
#     print 'alpha = ', i
#     #print 'Coefficients: \n', clf.coef_
#     #print("Residual sum of squares: %.2f" % np.mean((clf.predict(temperaturaMSTest) - temperaturaMGMinTest) ** 2))
#     #print('Variance score: %.2f' % clf.score(temperaturaMSTest, temperaturaMGMinTest))
#     scores = cross_validation.cross_val_score(clf, temperaturaMS, temperaturaMGMin, cv=10)
#     print("Accuracy: %0.4f (+/- %0.2f)" % (scores.mean(), scores.std() * 2))
#
#     clf = linear_model.Ridge(alpha=i)
#     #clf.fit(temperaturaMSTrain, temperaturaMGMaxTrain)
#
#     print 'Max Temperature'
#     print 'alpha = ', i
#     #print 'Coefficients: \n', clf.coef_
#     #print("Residual sum of squares: %.2f" % np.mean((clf.predict(temperaturaMSTest) - temperaturaMGMaxTest) ** 2))
#     #print('Variance score: %.2f' % clf.score(temperaturaMSTest, temperaturaMGMaxTest))
#     scores = cross_validation.cross_val_score(clf, temperaturaMS, temperaturaMGMax, cv=10)
#     print("Accuracy: %0.4f (+/- %0.2f)" % (scores.mean(), scores.std() * 2))