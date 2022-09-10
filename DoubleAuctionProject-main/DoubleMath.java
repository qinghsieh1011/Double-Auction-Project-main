import java.math.BigDecimal;
import java.math.RoundingMode; 

/**
 * DoubleMath
 */
public class DoubleMath {

    //預設除法運算精度 
    private static final int DEF_DIV_SCALE = 2;

    public static double add(double v1,double v2){ 
        BigDecimal b1 = new BigDecimal(Double.toString(v1)); 
        BigDecimal b2 = new BigDecimal(Double.toString(v2)); 
        return b1.add(b2).setScale(DEF_DIV_SCALE,RoundingMode.CEILING).doubleValue();
    }

    public static double sub(double v1,double v2){ 
        BigDecimal b1 = new BigDecimal(Double.toString(v1)); 
        BigDecimal b2 = new BigDecimal(Double.toString(v2)); 
        return b1.subtract(b2).setScale(DEF_DIV_SCALE,RoundingMode.CEILING).doubleValue(); 
    }

    public static double mul(double v1,double v2){ 
        BigDecimal b1 = new BigDecimal(Double.toString(v1)); 
        BigDecimal b2 = new BigDecimal(Double.toString(v2)); 
        return b1.multiply(b2).setScale(DEF_DIV_SCALE,RoundingMode.CEILING).doubleValue(); 
    }

    public static double div(double v1,double v2){ 
        return div(v1,v2,DEF_DIV_SCALE); 
    }

    // 當發生除不盡的情況時，由scale引數指定精度，以後的數字四捨五入。
    public static double div(double v1,double v2,int scale){ 
        if(scale < 0){ 
        throw new IllegalArgumentException("The scale must be a positive integer or zero"); 
        } 
        BigDecimal b1 = new BigDecimal(Double.toString(v1)); 
        BigDecimal b2 = new BigDecimal(Double.toString(v2)); 
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue(); 
    }

    // 提供精確的小數位四捨五入處理。
    public static double round(double v,int scale){ 
        if(scale < 0){ 
            throw new IllegalArgumentException( "The scale must be a positive integer or zero"); 
        } 
        BigDecimal b = new BigDecimal(Double.toString(v)); 
        BigDecimal one = new BigDecimal("1"); 
        return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue(); 
    }

    // 提供精確的小數位無條件捨去處理。
    public static double floor(double v,int scale){ 
        if(scale < 0){ 
            throw new IllegalArgumentException( "The scale must be a positive integer or zero"); 
        } 
        BigDecimal b = new BigDecimal(Double.toString(v)); 
        BigDecimal one = new BigDecimal("1"); 
        return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue(); 
    }

    // 提供精確的小數位無條件進位處理。
    public static double ceil(double v,int scale){ 
        if(scale < 0){ 
            throw new IllegalArgumentException( "The scale must be a positive integer or zero"); 
        } 
        BigDecimal b = new BigDecimal(Double.toString(v)); 
        BigDecimal one = new BigDecimal("1"); 
        return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue(); 
    }
}