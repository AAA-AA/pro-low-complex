 # -*- coding: utf-8 -*-
 #RandomForestClassifier
 import math
 import sys
 import matplotlib as mpl
 import warnings
 import requests
 import json
 import re
 import numpy as np
 from sklearn import tree
 from sklearn import ensemble
 from sklearn import metrics
 from sklearn.metrics import auc
 from sklearn.metrics import confusion_matrix
 from sklearn.metrics import precision_recall_curve
 from sklearn.metrics import classification_report
 from sklearn.model_selection import train_test_split
 from sklearn.model_selection import cross_val_score
 from sklearn.ensemble import RandomForestClassifier
 from sklearn.ensemble import ExtraTreesClassifier
 from sklearn.tree import DecisionTreeClassifier
 from sklearn.externals import joblib



 #########################-*-The function of fac_cpx-*-#########################
 def fac_cpx(w):
 		keep_str = []
 		for j in range(len(w)+1):
 				for i in range(len(w)-j+1):
 						keep_str.append(w[i:i+j])
 		keep_str = list(filter(None,keep_str))
 		keep_str_set = set(keep_str)
 		keep_str_sorted = sorted(keep_str_set,key = lambda i:len(i),reverse=False)
 		keep_num = []
 		for i in range(1,len(w)+1):
 				temporary_list =[]
 				for j in range(len(keep_str_sorted)):
 						if len(keep_str_sorted[j]) == i:
 								temporary_list.append(keep_str_sorted[j])
 				keep_num.append(temporary_list)
 		fac_num = []
 		for i in range(len(keep_num)):
 				fac_num.append(len(keep_num[i]))
 		return fac_num

 #########################-*-The function of abe_cpx-*-#########################
 def get_set_number(a):			#get Abe_cpx combination not the rank
 	temporary_deposit  = []
 	for i in range(len(a)):
 		a[i] = sorted(a[i])
 		a[i] = ''.join(a[i])
 		temporary_deposit.append(a[i])
 	temporary_deposit_set = set(temporary_deposit)
 	temporary_deposit_set_length = len(temporary_deposit_set)
 	return temporary_deposit_set_length

 def abe_cpx(w):
 		keep_str = []
 		for j in range(len(w)+1):
 				for i in range(len(w)-j+1):
 						keep_str.append(w[i:i+j])
 		keep_str = list(filter(None,keep_str))  #filter "" of all examples
 		keep_str_set = set(keep_str)
 		keep_str_set = sorted(keep_str_set,key = lambda i:len(i),reverse=False)
 		list_num = []
 		for i in range(1,len(w)+1):
 				list_temporary =[]
 				for j in range(len(keep_str_set)):
 						if len(keep_str_set[j]) == i:
 								list_temporary.append(keep_str_set[j])
 				list_num.append(list_temporary)
 		Abe_num = []
 		for i in range(len(list_num)):
 			   Abe_num.append(get_set_number(list_num[i])) #the function get the combination
 		return Abe_num

 def fac_abe(w):
 	fac = fac_cpx(w)
 	abe = abe_cpx(w)
 def IDR_str(file_name):
 	x_IDR = ""
 	with open(file_name,'r') as file_r:
 		for line in file_r.readlines():
 			line = line.strip('\n')
 			if line.startswith('>'):
 				pass
 			else:
 				x_IDR += line
 	return x_IDR

 #extract_paras from json
 def extract_paras(paras):
 	patten = r"(\d+(\.\d+)?)"
 	params = re.findall(patten,paras)
 	complexType = int(params[0][0])
 	slidewindow = int(params[1][0])
 	threshold = float(params[2][0])
 	if complexType == 1:
 		complexity = 'Fac'
 	elif complexType== 2:
 		complexity="Abe"
 	elif complexType== 3:
 		complexity ="FA"
 	return complexity,slidewindow,threshold
 #服务端输入的参数
 json_paras = sys.argv[1]
 Model_type,s_window,threshold = extract_paras(json_paras)


 def Predict_result_from_file(file_name,Model_type,s_window,threshold=0.95):
 	Model_name =  s_window + '_'+Model_type+'_train_model.m'
 	clf = joblib.load(Model_name)
 	x_IDR = IDR_str(file_name)
 	y_predict_label = ""
 	y_IDR_predict = []
 	for L in range(len(x_IDR)-s_window+1):
 		IDR_WL = x_IDR[L:L+s_window]
 		if Model_type =="Fac":
 			IDR_WL_fac = fac_cpx(IDR_WL)
 			IDR_WL_FULL_SEQ = []
 			IDR_WL_FULL_SEQ.append(IDR_WL_fac)
 			IDR_WL_FULL_SEQ = np.array(IDR_WL_FULL_SEQ)
 			y_IDR_predict_proba = clf.predict_proba(IDR_WL_FULL_SEQ)
 			y_IDR_predict.extend([round(x[-1],2) for x in y_IDR_predict_proba])
 		if Model_type =="Abe":
 			IDR_WL_abe = abe_cpx(IDR_WL)
 			IDR_WL_FULL_SEQ = []
 			IDR_WL_FULL_SEQ.append(IDR_WL_abe)
 			IDR_WL_FULL_SEQ = np.array(IDR_WL_FULL_SEQ)
 			y_IDR_predict_proba = clf.predict_proba(IDR_WL_FULL_SEQ)
 			y_IDR_predict.extend([round(x[-1],2) for x in y_IDR_predict_proba])
 		if Model_type=="FA":
 			IDR_WL_FA = fac_cpx(IDR_WL)+abe_cpx(IDR_WL)
 			IDR_WL_FULL_SEQ = []
 			IDR_WL_FULL_SEQ.append(IDR_WL_FA)
 			IDR_WL_FULL_SEQ = np.array(IDR_WL_FULL_SEQ)
 			y_IDR_predict_proba = clf.predict_proba(IDR_WL_FULL_SEQ)
 			y_IDR_predict.extend([round(x[-1],2) for x in y_IDR_predict_proba])
 		print ('L:',L)
 	print ("y_IDR_predict:",y_IDR_predict)
 	for i in range(len(y_IDR_predict)):
 		if y_IDR_predict[i] <= threshold:
 			y_predict_label += '0'
 		elif y_IDR_predict[i] > threshold:
 			y_predict_label += '1'
 	return y_predict_label



 #y_predict_label = Predict_result('RF_train_model.m','BRD4.txt',0.95)
 #print ("y_predict_label:",y_predict_label)
 #print (len(y_predict_label))


 def Predict_result_from_seq(singal_seq,Model_type,s_window,threshold=0.95):
 	Model_name =  '../model/'+str(s_window) + '_'+Model_type+'_train_model.m'
 	clf = joblib.load(Model_name)
 	x_IDR = singal_seq
 	y_predict_label = ""
 	y_IDR_predict = []
 	for L in range(len(x_IDR)-s_window+1):
 		IDR_WL = x_IDR[L:L+s_window]
 		if Model_type =="Fac":
 			IDR_WL_fac = fac_cpx(IDR_WL)
 			IDR_WL_FULL_SEQ = []
 			IDR_WL_FULL_SEQ.append(IDR_WL_fac)
 			IDR_WL_FULL_SEQ = np.array(IDR_WL_FULL_SEQ)
 			y_IDR_predict_proba = clf.predict_proba(IDR_WL_FULL_SEQ)
 			y_IDR_predict.extend([round(x[-1],2) for x in y_IDR_predict_proba])
 		if Model_type =="Abe":
 			IDR_WL_abe = abe_cpx(IDR_WL)
 			IDR_WL_FULL_SEQ = []
 			IDR_WL_FULL_SEQ.append(IDR_WL_abe)
 			IDR_WL_FULL_SEQ = np.array(IDR_WL_FULL_SEQ)
 			y_IDR_predict_proba = clf.predict_proba(IDR_WL_FULL_SEQ)
 			y_IDR_predict.extend([round(x[-1],2) for x in y_IDR_predict_proba])
 		if Model_type=="FA":
 			IDR_WL_FA = fac_cpx(IDR_WL)+abe_cpx(IDR_WL)
 			IDR_WL_FULL_SEQ = []
 			IDR_WL_FULL_SEQ.append(IDR_WL_FA)
 			IDR_WL_FULL_SEQ = np.array(IDR_WL_FULL_SEQ)
 			y_IDR_predict_proba = clf.predict_proba(IDR_WL_FULL_SEQ)
 			y_IDR_predict.extend([round(x[-1],2) for x in y_IDR_predict_proba])
 	#print ("y_IDR_predict:",y_IDR_predict)
 	for i in range(len(y_IDR_predict)):
 		if y_IDR_predict[i] <= threshold:
 			y_predict_label += '0'
 		elif y_IDR_predict[i] > threshold:
 			y_predict_label += '1'
 	return y_predict_label



 def main():
 	test_seq = "ATWTTTTTTTTBVCODUHOOFEOUHHHHD"
 	#print(Model_type,s_window,threshold)
 	Predict_label= Predict_result_from_seq(test_seq,Model_type,s_window,threshold)
 	np.save('pred_results.txt',Predict_label)
 	#print(Predict_label)
 	#print(model_name)
 	print("pred_results.txt")
 	print("easyplot.png")






 if __name__ =="__main__":
 	main()