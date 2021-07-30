package entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author Shariar (Shawn) Emami
 */
@Converter
public class RhesusFactorConvertor implements AttributeConverter<RhesusFactor, String> {

    @Override
    public String convertToDatabaseColumn( RhesusFactor attribute ) {
        return attribute.getSymbol();
    }

    @Override
    public RhesusFactor convertToEntityAttribute( String dbData ) {
        return RhesusFactor.getRhesusFactor( dbData );
    }
}
