package me.turt2live.meaSuite.Math;

/**
 * Defines an mathematical operator.
 * 
 * @author Marcos A. Vasconcelos Junior
 * @author Marco Biscaro
 */
public enum Operator {
	PLUS("+", Operands.DOUBLE, 0) {
		@Override
		public Double resolve(Double f1, Double f2) {
			return f1 + f2;
		}
	},
	MINUS("-", Operands.DOUBLE, 0) {
		@Override
		public Double resolve(Double f1, Double f2) {
			return f1 - f2;
		}

	},
	TIMES("*", Operands.DOUBLE, 10) {
		@Override
		public Double resolve(Double f1, Double f2) {
			return f1 * f2;
		}

	},
	DIV("/", Operands.DOUBLE, 10) {
		@Override
		public Double resolve(Double f1, Double f2) {
			return f1 / f2;
		}

	},
	POW("^", Operands.DOUBLE, 10) {
		@Override
		public Double resolve(Double f1, Double f2) {
			return Math.pow(f1, f2);
		}

	},
	MOD("%", Operands.DOUBLE, 10) {
		@Override
		public Double resolve(Double f1, Double f2) {
			return f1 % f2;
		}

	},
	COS("cos", Operands.SINGLE, 20) {
		@Override
		public Double resolve(Double f1, Double f2) {
			return Math.cos(f1);
		}

	},
	SIN("sin", Operands.SINGLE, 20) {
		@Override
		public Double resolve(Double f1, Double f2) {
			return Math.sin(f1);
		}

	},
	TAN("tan", Operands.SINGLE, 20) {
		@Override
		public Double resolve(Double f1, Double f2) {
			return Math.tan(f1);
		}

	},
	ACOS("acos", Operands.SINGLE, 20) {
		@Override
		public Double resolve(Double f1, Double f2) {
			return Math.acos(f1);
		}

	},
	ASIN("asin", Operands.SINGLE, 20) {
		@Override
		public Double resolve(Double f1, Double f2) {
			return Math.asin(f1);
		}

	},
	ATAN("atan", Operands.SINGLE, 20) {
		@Override
		public Double resolve(Double f1, Double f2) {
			return Math.atan(f1);
		}

	},
	SQRT("sqrt", Operands.SINGLE, 20) {
		@Override
		public Double resolve(Double f1, Double f2) {
			return Math.sqrt(f1);
		}

	},
	SQR("sqr", Operands.SINGLE, 20) {
		@Override
		public Double resolve(Double f1, Double f2) {
			return f1 * f1;
		}

	},
	LOG("log", Operands.SINGLE, 20) {
		@Override
		public Double resolve(Double f1, Double f2) {
			return Math.log(f1);
		}

	},
	FLOOR("floor", Operands.SINGLE, 20) {
		@Override
		public Double resolve(Double f1, Double f2) {
			return Math.floor(f1);
		}

	},
	CEIL("ceil", Operands.SINGLE, 20) {
		@Override
		public Double resolve(Double f1, Double f2) {
			return Math.ceil(f1);
		}

	},
	ABS("abs", Operands.SINGLE, 20) {
		@Override
		public Double resolve(Double f1, Double f2) {
			return Math.abs(f1);
		}

	},
	NEG("neg", Operands.SINGLE, 20) {
		@Override
		public Double resolve(Double f1, Double f2) {
			return -f1;
		}

	},
	RND("rnd", Operands.SINGLE, 20) {
		@Override
		public Double resolve(Double f1, Double f2) {
			return Math.random() * f1;
		}
	},
	RAD("rad", Operands.SINGLE, 20) {
		@Override
		public Double resolve(Double f1, Double f2) {
			return Math.toRadians(f1);
		}
	},
	DEG("deg", Operands.SINGLE, 20) {
		@Override
		public Double resolve(Double f1, Double f2) {
			return Math.toDegrees(f1);
		}
	},
	AND("&", Operands.DOUBLE, 30) {
		@Override
		public Double resolve(Double f1, Double f2) {
			return (double) ((int) Math.floor(f1) & (int) Math.floor(f2));
		}
	},
	OR("|", Operands.DOUBLE, 30) {
		@Override
		public Double resolve(Double f1, Double f2) {
			return (double) ((int) Math.floor(f1) | (int) Math.floor(f2));
		}
	};

	private String		op;
	private Operands	type;
	private int			priority;

	private Operator(String op, Operands type, int p) {
		this.op = op;
		this.type = type;
		this.priority = p;
	}

	public abstract Double resolve(Double f1, Double f2);

	/**
	 * Operator's type.
	 * 
	 * @author Marcos A. Vasconcelos Junior
	 */
	public enum Operands {
		SINGLE,
		DOUBLE;
	}

	public String getOperator() {
		return this.op;
	}

	public Operands getType() {
		return this.type;
	}

	public int getPriority() {
		return this.priority;
	}

}