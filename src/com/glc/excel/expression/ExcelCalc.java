package com.glc.excel.expression;


import java.util.logging.Logger;

public class ExcelCalc {
	private static Logger log = Logger.getLogger("lavasoft");

	public void test1() {

		int type = 5;
		String beginDate = "K2";// 计划开始日期
		String day = "4";// 往前（负值）/后（正值）多少天日期
		String systemDate = null;// 系统日期
		String endDate = "N2";// 计划完成日期
		String acTualFinish = "T2";// 实际完成百分比
		String holidays = "hd!B$2:hd!B$60";// 为空时默认（不剔除节假日，周六周日）
		rateStatusExpression(type, beginDate, day, systemDate, endDate, acTualFinish, holidays);
		// fulfillment(4, "K2",0, null, "N2","R2", "hd!B$2:hd!B$60");
		// fulfillment(5, "K2","", null, "N2","R2", "hd!B$2:hd!B$60");

	}

	/**
	 * type 1.返回开始日期N天后的结束日期 2.返回开始日期到结束日期的天数 3.返回当前系统进行了多少天 4.返回当前系统的进度百分比
	 * 5.返回系统正常/延期天数/进行中/完成 beginDate 计划开始日期 day 往前（负值）/后（正值）多少天日期 systemDate 系统日期
	 * endDate 计划完成日期 acTualFinish 实际完成百分比 holidays 为空时默认（不剔除节假日，周六周日）
	 */
	public String rateStatusExpression(int type, String beginDate, String day, String systemDate, String endDate,
									   String acTualFinish, String holidays) {
		beginDate = beginDate == null ? "K2" : beginDate;// 开始时间
		systemDate = systemDate == null ? "hd!D$2" : systemDate;// 系统时间
		endDate = endDate == null ? "N2" : endDate;// 结束时间
		acTualFinish = acTualFinish == null ? "R2" : acTualFinish;// 实际完成百分比
		// hd节假日sheet页
		if (holidays != null && !holidays.contains(")")) {
			holidays = holidays == null ? null : "," + holidays + ")";// 节假日
		}
		System.out.println("holidays=================" + holidays);
		if (type == 1)
//			return System.getProperty("user.dir")+ File.separator+"config"+File.separator+"config.properties";
			return getBeginOrEndDate(beginDate, day, holidays);
		if (type == 2)
			return getBeginSubtrEndDate(beginDate, endDate, holidays);
		if (type == 3)
			return getSysDateOngoCountDay(beginDate, systemDate, holidays);
		String planDays = rateStatusExpression(2, beginDate, day, systemDate, endDate, acTualFinish, holidays);// 计划总天数
		String physDays = rateStatusExpression(3, beginDate, day, systemDate, endDate, acTualFinish, holidays);// 实际总天数
		if (type == 4)
			return getSysDateRatePercentage(planDays, physDays, acTualFinish);
		// 返回计划完成天数
		if (type == 5)
			return getSysDateStatus(planDays, physDays, acTualFinish);
		return "没有该选项";
	}

	/**
	 * 1.计算开始日期N天后的结束日期（不包含开始当天,剔除周六周日）
	 *
	 * @param beginDate
	 * @param day
	 * @param holidays
	 * @return 开始日期N天后的结束日期
	 */
	public String getBeginOrEndDate(String beginDate, String day, String holidays) {

		String getBeginOrEndDate = "";
		System.out.println(
				"===============================[\t" + getBeginOrEndDate + "\t]===============================");

		if (beginDate != null && day != null) {

			try{
				int intDay = Integer.valueOf(day);
				if (intDay > 0) {
					intDay = intDay - 1;
				} else if (intDay < 0) {
					intDay = intDay + 1;
				}
				day = intDay+"";
			}catch (Exception e){
				System.out.println("day=数字天数："+day);
			}
			getBeginOrEndDate = "WORKDAY(" + beginDate + "," + day + ")";// 实际结束日期
			if (holidays != null)
				getBeginOrEndDate = getBeginOrEndDate.replace(")", holidays);
			System.out.println("=" + getBeginOrEndDate);
		} else {
			System.out.println("1.计算开始日期N天后的结束日期====beginDate&&day====均不能为空");
			return "1.计算开始日期N天后的结束日期====计划开始日期&&天数====均不能为空";
		}
		System.out.println(
				"===============================[\t" + getBeginOrEndDate + "\t]===============================");
		return getBeginOrEndDate;
	}

	/**
	 * 2.计算开始日期到结束日期的天数(剔除周六周日)
	 *
	 * @param beginDate
	 * @param endDate
	 * @param holidays
	 * @return 开始日期到结束日期的天数
	 */
	public String getBeginSubtrEndDate(String beginDate, String endDate, String holidays) {
		String getBeginSubtrEndDate = "";
		System.out.println("===============================[\tgetBeginSubtrEndDate\t]===============================");
		if (beginDate != null && endDate != null) {
			getBeginSubtrEndDate = "NETWORKDAYS(" + beginDate + "," + endDate + ")";// 计划总天数
			if (holidays != null)
				getBeginSubtrEndDate = getBeginSubtrEndDate.replace(")", holidays);
			System.out.println("=" + getBeginSubtrEndDate);
		} else {
			System.out.println("2.计算开始日期到结束日期的天数====beginDate&&endDate====均不能为空");
			return "2.计算开始日期到结束日期的天数====计划开始日期&&计划完成日期====均不能为空";
		}
		System.out.println("===============================[\tgetBeginSubtrEndDate\t]===============================");
		return getBeginSubtrEndDate;
	}

	/**
	 * 3.计算当前任务实际进行了多少天(剔除周六周日)
	 *
	 * @param beginDate
	 * @param systemDate
	 * @param holidays
	 * @return 当前任务进行了多少天
	 */
	public String getSysDateOngoCountDay(String beginDate, String systemDate, String holidays) {
		String getSysDateOngoCountDay = "";
		System.out
				.println("===============================[\tgetSysDateOngoCountDay\t]===============================");
		if (beginDate != null && systemDate != null) {
			getSysDateOngoCountDay = "NETWORKDAYS(" + beginDate + "," + systemDate + ")";// 实际总天数
			if (holidays != null)
				getSysDateOngoCountDay = getSysDateOngoCountDay.replace(")", holidays);
			System.out.println("=" + getSysDateOngoCountDay);
		} else {
			System.out.println("3.计算当前任务实际进行了多少天====beginDate&&systemDate====均不能为空");
			return"3.计算当前任务实际进行了多少天====计划开始日期&&系统日期====均不能为空";
		}
		System.out.println("===============================[\tgetSysDateOngoCountDay\t]===============================");
		return getSysDateOngoCountDay;
	}

	/**
	 * 4.计算当前任务的进度百分比
	 *
	 * @param planDays
	 * @param physDays
	 * @param acTualFinish
	 * @return 当前系统的进度百分比
	 */
	public String getSysDateRatePercentage(String planDays, String physDays, String acTualFinish) {
		String getSysDateRatePercentage = "";
		System.out.println(
				"===============================[\tgetSysDateRatePercentage\t]===============================");
		if (planDays != null && physDays != null && acTualFinish != null) {
			String scale_Phys_Plan = physDays + "/" + planDays;
			System.out.println("scale_Phys_Plan========" + scale_Phys_Plan);
			// 实际总天数/计划总天数>0 时，返回 实际总天数/计划总天数， 否则 ABS(实际总天数/计划总天数)未开始
			String conditionGt = conditionIF(scale_Phys_Plan, ">", "0");// 实际总天数/计划总天数>0
			System.out.println("conditionGt========" + conditionGt);
			String expressionGt = expressionIF(conditionGt, scale_Phys_Plan, appendAbs(scale_Phys_Plan));
			System.out.println("expressionIFLt========" + expressionGt);
			// 实际天数<=0 为开始
			String conditionLt = conditionIF(physDays, "<=", "0");//
			expressionGt = expressionIF(conditionLt, "0", expressionGt);
			conditionGt = conditionIF(expressionGt, ">=", "1");// 计划完成比例
			expressionGt = expressionIF(conditionGt, "1", expressionGt);
			conditionGt = conditionIF(acTualFinish, ">=", "1");// 实际完成比例比较
			getSysDateRatePercentage = expressionIF(conditionGt, "1", expressionGt);
			System.out.println("=" + getSysDateRatePercentage);
		} else {
			System.out.println("4.计算当前任务的进度百分比====planDays&&physDays&&acTualFinish====均不能为空");

			return "4.计算当前任务的进度百分比====实际百分比===均不能为空";
		}
		System.out.println(
				"===============================[\tgetSysDateRatePercentage\t]===============================");
		return getSysDateRatePercentage;
	}

	/**
	 * 5.检索当前任务进行状态
	 *
	 * @param planDays
	 *            计划天数
	 * @param physDays
	 *            实际天数
	 * @param acTualFinish
	 *            实际完成百分比
	 * @return 系统正常/延期天数/进行中/完成
	 */
	public String getSysDateStatus(String planDays, String physDays, String acTualFinish) {
		String getSysDateStatus = "";
		System.out.println("===============================[\tgetSysDateStatus\t]===============================");
		if (planDays != null && physDays != null && acTualFinish != null) {
			String conditionLt = conditionIF(physDays, "<=", "0");// 实际天数<=0，未开始
			System.out.println("conditionLt========" + conditionLt);
			String conditionGt = conditionIF(physDays, ">", "0");// 实际天数>0，开始
			System.out.println("conditionGt========" + conditionGt);
			String conditionYqTsGt = conditionIF(planDays, ">", physDays);// 计划天数>实际天数，正常天数， 延期N天（实际天数-计划天数）
			System.out.println("conditionYqTsGt========" + conditionYqTsGt);
			String plan_physDay = planDays + "-" + physDays;// 计划天数-实际天数
			String expressionYqTsGt = expressionIF(conditionYqTsGt, physDays, plan_physDay);
			System.out.println("expressionYqTsGt========" + expressionYqTsGt);
			String conditionEqual = conditionIF(plan_physDay, "=", "0");// 计划天数-实际天数=0，完成999，expressionYqTsGt
			System.out.println("conditionEqual========" + conditionEqual);
			String expressionEqual = expressionIF(conditionEqual, "999", expressionYqTsGt);
			System.out.println("expressionEqual========" + expressionEqual);
			String expressionGt = expressionIF(conditionGt, expressionEqual, "888888");
			System.out.println("expressionGt========" + expressionGt);
			// 延期/正常天数转换
			// expressionGt>0
			String expressionWC = conditionIF(expressionGt, "=", "999");// 完成
			String expressionJXZ = conditionIF(expressionGt, "=", "888888");// 未开始
			String expressionLtWKS = conditionIF(expressionGt, "<", "0");// 延期
			String expressionGtYQ = conditionIF(expressionGt, ">", "0");// 进行中
			String expressionGtYQ1 = expressionIF(expressionGtYQ, "\"进行\"&" + expressionGt + "&\"天\"", expressionGt);
			String expressionLtWKS1 = expressionIF(expressionLtWKS, "\"延期\"&" + appendAbs(expressionGt) + "&\"天\"",
					expressionGtYQ1);
			String expressionJXZ1 = expressionIF(expressionJXZ, "\"未开始\"", expressionLtWKS1);
			getSysDateStatus = expressionIF(expressionWC, "\"完成\"", expressionJXZ1);

			conditionGt = conditionIF(acTualFinish, "=", "1");// 实际完成比例比较
			getSysDateStatus = expressionIF(conditionGt, "\"完成\"", getSysDateStatus);
			System.out.println("=" + getSysDateStatus);
		} else {
			System.out.println("5.检索当前任务进行状态====planDays&&physDays&&acTualFinish====均不能为空");
			return "5.检索当前任务进行状态====实际百分比====均不能为空";

		}
		System.out.println("===============================[\tgetSysDateStatus\t]===============================");
		return getSysDateStatus;
	}

	/**
	 * 拼接IF条件
	 *
	 * @param valueA
	 *            比较数值A
	 * @param mark
	 *            比较符号
	 * @param valueB
	 *            比较数值B
	 * @return IF条件
	 */
	public String conditionIF(String valueA, String mark, String valueB) {
		System.out.println("conditionIF========" + valueA + mark + valueB);
		return valueA + mark + valueB;
	}

	/**
	 * IF表达式拼接
	 *
	 * @param condition
	 *            条件
	 * @param trueValue
	 *            真值
	 * @param falseValue
	 *            假值
	 * @return IF表达式
	 */
	public String expressionIF(String condition, String trueValue, String falseValue) {
		String expressionIF = "IF(" + condition + "," + trueValue + ")";
		if (falseValue != null) {
			expressionIF = expressionIF.substring(0, expressionIF.lastIndexOf(")")) + "," + falseValue + ")";
		}
		System.out.println("expressionIF========" + expressionIF);
		return expressionIF;
	}

	/**
	 * 绝对值拼接
	 *
	 * @param absValue
	 * @return 绝对值表达式
	 */
	public String appendAbs(String absValue) {
		return "ABS(" + absValue + ")";
	}

//	public boolean isNumber(String string) {
//		Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");
//		return pattern.matcher(string).matches();
//	}

}
