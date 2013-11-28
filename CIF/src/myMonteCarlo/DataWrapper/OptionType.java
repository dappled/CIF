package myMonteCarlo.DataWrapper;

import java.io.Serializable;

public class OptionType implements Serializable {

	private static final long		serialVersionUID	= 1L;

	public static final OptionType	EuropeanCall		= new OptionType( "EuropeanCall" );
	public static final OptionType	AsianCall			= new OptionType( "AsianCall" );

	private String					_optionType;

	private OptionType(String optionType) {
		_optionType = optionType;
	}

	@Override
	public String toString() {
		return (String.format( "%s", _optionType ));
	}
}
