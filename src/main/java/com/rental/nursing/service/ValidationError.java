package com.rental.nursing.service;

public class ValidationError {
	/*
	 * VE is ValidationError, EE is EmployerError, RE is RatingError
	 * 
	 */
	public static final String VE001 = "VE001: null";
	public static final String VE002 = "VE002: long";
	public static final String VE003 = "VE003: short";
	public static final String VE004 = "VE004: rating";
	public static final String VE005 = "VE005: user";
	public static final String VE006 = "VE006: date";
	public static final String VE007 = "VE007: range";
	public static final String VE008 = "VE008: notvalid";
	public static final String EE101 = "EE101: Employer not found ";
	public static final String EE102 = "EE102: Employer data validation failed ";
	public static final String EE103 = "EE103: Corrupt employer data ";
	public static final String EE201 = "EE201: Employer save error ";
	public static final String EE202 = "EE202: Employer update error ";
	public static final String EE203 = "EE203: Employer deletion error ";
	public static final String NE101 = "NE101: Nurse not found ";
	public static final String NE102 = "NE102: Nurse data validation failed ";
	public static final String NE103 = "NE103: Corrupt Nurse data ";
	public static final String NE201 = "NE201: Nurse save error ";
	public static final String NE202 = "NE202: Nurse update error ";
	public static final String NE203 = "NE203: Nurse deletion error ";
	public static final String RE101 = "RE101: EmployerRating not found ";
	public static final String RE102 = "RE102: EmployerRating data validation failed ";
	public static final String RE103 = "RE103: Corrupt EmployerRating data ";
	public static final String RE201 = "RE201: EmployerRating save error ";
	public static final String RE202 = "RE202: EmployerRating update error ";
	public static final String RE203 = "RE203: EmployerRating deletion error ";
	public static final String JE101 = "JE101: Job not found ";
	public static final String JE102 = "JE102: Job data validation failed ";
	public static final String JE103 = "JE103: Corrupt Job data ";
	public static final String JE201 = "JE201: Job save error ";
	public static final String JE202 = "JE202: Job update error ";
	public static final String JE203 = "JE203: Job deletion error ";
}
