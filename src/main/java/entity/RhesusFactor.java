package entity;

import java.util.Objects;

/**
 *
 * @author Shariar (Shawn) Emami
 */
public enum RhesusFactor {
    Positive( "+" ), Negative( "-" );

    private final String symbol;

    private RhesusFactor( String value ) {
        this.symbol = value;
    }

    public String getSymbol() {
        return symbol;
    }

    public static RhesusFactor getRhesusFactor( String symbol ) {
        Objects.requireNonNull( symbol, "RhesusFactor cannot have null symbol" );
        RhesusFactor[] values = values();
        for( RhesusFactor value: values ) {
            if( value.getSymbol().equalsIgnoreCase( symbol ) ){
                return value;
            }
        }
        RhesusFactor rhd = RhesusFactor.valueOf( symbol );
        if( rhd != null ) {
            return rhd;
        }
        throw new IllegalArgumentException( "Symbole=\"" + symbol + "\" does not exists in RhesusFactor" );
    }
}
