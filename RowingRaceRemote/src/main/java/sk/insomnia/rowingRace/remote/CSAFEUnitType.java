package sk.insomnia.rowingRace.remote;

public enum CSAFEUnitType {
	MILE(1),
	TENTH_OF_MILE(2),
	HUNDREDTH_OF_MILE(3),
	THOUSANDTH_OF_MILE(4),
	FEET(5),
	INCH(6),
	POUNDS(7),
	TENTH_OF_POUND(8),
	TEN_FEET(10),
	MILE_PER_HOUR(16),
	TENTH_OF_MILE_PER_HOUR(17),
	HUNDRETH_OF_MILE_PER_HOUR(18),
	FEET_PER_MINUTE(19),
	KILOMETERS(33),
	TENTH_OF_KILOMETER(34),
	HUNDREDTH_OF_KILOMETER(35),
	METER(36),
	TENTH_OF_METER(37),
	CENTIMITER(38),
	KILOGRAM(39),
	TENTH_OF_KILOGRAM(40),
	KILOMETER_PER_HOUR(48),
	TENTH_OF_KILOMETER_PER_HOUR(49),
	HUNDREDTH_OF_KILOMETER_PER_HOUR(50),
	METER_PER_MINUTE(51),
	MINUTES_PER_MILE(55),
	MINUTES_PER_KILOMETER(56),
	SECONDS_PER_KILOMETER(57),
	SECONDS_PER_MILE(58),
	FLOORS(65),
	TENTH_OF_FLOORS(66),
	STEPS(67),
	REVOLUTIONS(68),
	STRIDES(69),
	STROKES(70),
	BEATS(71),
	CALORIES(72),
	KP(73),
	PERCENTGRADE(74),
	HUNDRETH_PER_CENTOFGRADE(75),
	TENTH_OF_GRADE(76),
	TENTH_OF_FLOORS_PER_MINUTE(79),
	FLOORS_PER_MINUTE(80),
	STEPS_PER_MINUTE(81),
	REVS_PER_MINUTE(82),
	STRIDES_PER_MINUTE(83),
	STOKES_PER_MINUTE(84),
	BEATS_PER_MINUTE(85),
	CALORIES_PER_MINUTE(86),
	CALORIES_PER_HOUR(87),
	WATTS(88),
	KPM(89),
	INCH_TO_POUND(90),
	FOOT_TO_POUND(91),
	NEWTONTOMETERS(92),
	AMPERES(97),
	THOUSANDTH_OF_AMPERE(98),
	VOLTS(99),
	THOUSANDTH_OF_VOLT(100);

	
	private int code;
	
	CSAFEUnitType (int code){
		this.code =code;  
	}
	
	public int getCode(){
		return this.code;
	}
	public String codeInHexa(){
		return Integer.toHexString(code);
	}
}