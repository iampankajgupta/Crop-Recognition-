//package in.my.cropmldetection;
//
//import retrofit2.Call;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.http.GET;
//
//public class CropDataApi {
//
//    public static final String key = "579b464db66ec23bdd000001da8948fedbb849f740cd62e90beab900";
//    public static final String url = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070";
//
////     I HAVE USED SINGLETON PATTERN  HERE SO THAT ONLY ONE INSTANCE OF CROPdATA SERVICE CREATE
//
//    public static CropDataService cropDataService = null;
//
//    public static CropDataService getCropDataService(){
//        if(cropDataService==null){
//
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(url)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//
//
//            cropDataService = retrofit.create(CropDataService.class);
//        }
//        return cropDataService;
//    }
//
//
//    public interface CropDataService{
//
//        @GET("?api-key="+key)
//        Call<CropData> getCropData();
//    }
//
//
//}
