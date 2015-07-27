import numpy as np
from sklearn import linear_model
from sklearn import cross_validation
from sklearn.externals import joblib
import sys

if len(sys.argv)>1:
    f = open(sys.argv[1], 'r')
else:
    print("Error: Input file missing.")
temperaturaMS = []
temperaturaMGMin = []
temperaturaMGMax = []
for line in f:
    variables = line.split(';')
    temperaturaMS.append(sorted([int(i) for i in variables[5].split(',')]))
    temperaturaMGMin.append(int(variables[8].split(',')[0]))
    temperaturaMGMax.append(int(variables[8].rstrip().split(',')[1]))
f.close()

# # Entrada
# temperaturaMSTrain = temperaturaMS[:-(len(temperaturaMS) / 5)]
# temperaturaMSTest = temperaturaMS[-(len(temperaturaMS) / 5):]
# #Salidas (Temp min y max)
# temperaturaMGMinTrain = temperaturaMGMin[:-(len(temperaturaMGMin) / 5)]
# temperaturaMGMinTest = temperaturaMGMin[-(len(temperaturaMGMin) / 5):]
# temperaturaMGMaxTrain = temperaturaMGMax[:-(len(temperaturaMGMax) / 5)]
# temperaturaMGMaxTest = temperaturaMGMax[-(len(temperaturaMGMax) / 5):]

temperaturaMSTrain, temperaturaMSTest, temperaturaMGMinTrain, temperaturaMGMinTest, temperaturaMGMaxTrain, temperaturaMGMaxTest = cross_validation.train_test_split(
    temperaturaMS, temperaturaMGMin, temperaturaMGMax, test_size=0.2, random_state=0)

# print ('Linear Regression:\n')

clf = linear_model.LinearRegression()
#clf.fit(temperaturaMSTrain, temperaturaMGMinTrain)

print ('Min Temperature')
#print 'Coefficients: \n', clf.coef_
#print("Residual sum of squares: %.2f" % np.mean((clf.predict(temperaturaMSTest) - temperaturaMGMinTest) ** 2))
#print('Variance score: %.2f' % clf.score(temperaturaMSTest, temperaturaMGMinTest))
scores = cross_validation.cross_val_score(clf, temperaturaMS, temperaturaMGMin, cv=10)
print("Accuracy: %0.4f (+/- %0.2f)" % (scores.mean(), scores.std() * 2))

# Save model
clf.fit(temperaturaMS, temperaturaMGMin)
joblib.dump(clf,"TemperaturaMin.regresion")

clf = linear_model.LinearRegression()
#clf.fit(temperaturaMSTrain, temperaturaMGMaxTrain)

print ('Max Temperature')
#print 'Coefficients: \n', clf.coef_
#print("Residual sum of squares: %.2f" % np.mean((clf.predict(temperaturaMSTest) - temperaturaMGMaxTest) ** 2))
#print('Variance score: %.2f' % clf.score(temperaturaMSTest, temperaturaMGMaxTest))
scores = cross_validation.cross_val_score(clf, temperaturaMS, temperaturaMGMax, cv=10)
print("Accuracy: %0.4f (+/- %0.2f)" % (scores.mean(), scores.std() * 2))

# Save model
clf.fit(temperaturaMS, temperaturaMGMax)
joblib.dump(clf,"TemperaturaMax.regresion")


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