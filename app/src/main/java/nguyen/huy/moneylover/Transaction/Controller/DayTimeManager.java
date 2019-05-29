package nguyen.huy.moneylover.Transaction.Controller;

public class DayTimeManager {

    public DayTimeManager() {

    }


    //Các hàm để xử lý về thu chi
    //Chuyển từ dạng dd/MM/yyyy -> dd+MM+yyyy
    public static String[] ConvertFormatDay(String ngay){
        String[] words=ngay.split("[/]");
        String[] resultString=new String[2];
        resultString[0]=words[1]+ "+" + words[2];
        resultString[1]= words[0]+ "+" +words[1]+ "+" + words[2];
        return resultString;
    }
    //Tách chuỗi từ dạng dd+MM+yyyy -> để lấy MM+yyyy
    public static String[] ConvertFormatDayGetMonth(String string){
        String[] words=string.split("[+]");
        String[] result=new String[2];
        result[0]=words[1]+ "+" + words[2];
        result[1]= words[0]+ "+" +words[1]+ "+" + words[2];
        return result;
    }

    //Tách chuỗi để lấy ngày tháng năm riêng biệt
    public static int[] splitDayMonthYear(String ngay){
        String[] words=ngay.split("[/]");
        int[] result=new int[3];
        result[0]=Integer.parseInt(words[0]);
        result[1]=Integer.parseInt(words[1]);
        result[2]=Integer.parseInt(words[2]);
        return result;
    }

    //Tách chuỗi để lấy tháng trước
    public static String[] splitDayGetPreviousMonth(String ngay){
        String[] words=ngay.split("[/]");
        int[] number=new int[3];
        number[0]=Integer.parseInt(words[0]);
        number[1]=Integer.parseInt(words[1]);
        number[2]=Integer.parseInt(words[2]);
        if(number[1]>1){
            number[1]=number[1]-1;
        }
        else if(number[1]==1){
            number[1]=12;
            number[2]=number[2]-1;
        }
        String[] result=new String[2];
        if(number[1]>0 && number[1]<10) {
            result[0] = "0"+number[1] + "+" + number[2];
        }
        else {
            result[0] = number[1] + "+" + number[2];
        }
        result[1] = words[0] + "+" + number[1] + "+" + number[2];
        return result;
    }

}
