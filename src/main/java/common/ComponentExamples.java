package common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Shariar (Shawn) Emami
 */
public class ComponentExamples {

    public static void main( String[] args ) throws ParseException {
        regexExample1();
        regexExample2();
        dateConversionExample();
    }

    public static void dateConversionExample() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd kk:mm:ss" );

        Date date1 = Date.from( Instant.now( Clock.systemDefaultZone() ) );
        Date date2 = Date.from( Instant.now( Clock.systemDefaultZone() ) );

        long timeInMilliSeconds1 = date1.getTime();
        long timeInMilliSeconds2 = date2.getTime();

        long errorRangeInMilliSeconds = 10000;//10 seconds

//        assertTrue( Math.abs( timeInMilliSeconds1 - timeInMilliSeconds2 ) < errorRangeInMilliSeconds );
        String strNow = formatter.format( date1 );
        System.out.println( strNow );

        String strNow2 = formatter.format( date2 );
        System.out.println( strNow2 );
    }

    public static void regexExample1() {
        String text = "/BoardTableJSP";

        String regex = "(/)+(.+)(TableJSP)+";
        Pattern pattern = Pattern.compile( regex );

        Matcher matcher = pattern.matcher( text );

        if( matcher.matches() ){
            for( int i = 0; i <= matcher.groupCount(); i++ ) {
                System.out.println( matcher.group( i ) );
            }
        }
    }

    public static void regexExample2() {
        String text = "/CreateAccountJSP";

        String regex = "(/Create)+(.+)(JSP)+";
        Pattern pattern = Pattern.compile( regex );

        Matcher matcher = pattern.matcher( text );

        if( matcher.matches() ){
            for( int i = 0; i <= matcher.groupCount(); i++ ) {
                System.out.println( matcher.group( i ) );
            }
        }
    }
}
