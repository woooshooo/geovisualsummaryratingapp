package com.thesis2.genise_villanueva.thesis;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.vatsal.imagezoomer.ZoomAnimation;


public class ImagesController {
    private static final String TAG = "ImagesController";
    private Context mContext;
    //tripadvisor
    private int abreeza[] = new int[]{R.drawable.abreeza_0,R.drawable.abreeza_1};
    private int aldevinco[] = new int[]{R.drawable.aldevinco_0,R.drawable.aldevinco_1};
    private int bankerohan[] = new int[]{R.drawable.bangkerohan_0,R.drawable.bangkerohan_1};
    private int bemwa[] = new int[]{R.drawable.bemwa_0,R.drawable.bemwa_1};
    private int bluejaz[] = new int[]{R.drawable.bluejaz_0,R.drawable.bluejaz_1};
    private int butterfly[] = new int[]{R.drawable.butterfly_0,R.drawable.butterfly_1};
    private int christina[] = new int[]{R.drawable.christina_0,R.drawable.christina_1};
    private int coralgarden[] = new int[]{R.drawable.coralgarden_0,R.drawable.coralgarden_1};
    private int crocodilepark[] = new int[]{R.drawable.crocodilepark_0,R.drawable.crocodilepark_1};
    private int davaomuseum[] = new int[]{R.drawable.davaomuseum_0,R.drawable.davaomuseum_1};
    private int dbone[] = new int[]{R.drawable.dbone_0,R.drawable.dbone_1};
    private int eden[] = new int[]{R.drawable.eden_0,R.drawable.eden_1};
    private int felcris[] = new int[]{R.drawable.felcris_0,R.drawable.felcris_1};
    private int gap[] = new int[]{R.drawable.gap_0,R.drawable.gap_1};
    private int giant[] = new int[]{R.drawable.clam_0,R.drawable.clam_1};
    private int gmallthepeak[] = new int[]{R.drawable.thepeak_0,R.drawable.thepeak_1};
    private int gmall[] = new int[]{R.drawable.gmall_0,R.drawable.gmall_1};
    private int golden[] = new int[]{R.drawable.goldenbay_0,R.drawable.goldenbay_1};
    private int gumamela[] = new int[]{R.drawable.gumamela_0,R.drawable.gumamela_1};
    private int hagimit[] = new int[]{R.drawable.hagimit_0,R.drawable.hagimit_1};
    private int japtunnel[] = new int[]{R.drawable.japtunnel_0,R.drawable.japtunnel_1};
    private int japcavehouse[] = new int[]{R.drawable.japcavehouse_0,R.drawable.japcavehouse_1};
    private int kaputian[] = new int[]{R.drawable.kaputian_0,R.drawable.kaputian_1};
    private int lolengs[] = new int[]{R.drawable.lolengs_0,R.drawable.lolengs_1};
    private int lonhua[] = new int[]{R.drawable.lonhua_0,R.drawable.lonhua_1};
    private int magsaysay[] = new int[]{R.drawable.magsaysay_0,R.drawable.magsaysay_1};
    private int malagos[] = new int[]{R.drawable.malagos_0,R.drawable.malagos_1};
    private int mts[] = new int[]{R.drawable.mts_0,R.drawable.mts_1};
    private int maxima[] = new int[]{R.drawable.maxima_0,R.drawable.maxima_1};
    private int monfort[] = new int[]{R.drawable.monfort_0,R.drawable.monfort_1};
    private int mtapo[] = new int[]{R.drawable.mtapo_0,R.drawable.mtapo_1};
    private int galintan[] = new int[]{R.drawable.galintan_0,R.drawable.galintan_1};
    private int putingbato[] = new int[]{R.drawable.putingbato_0,R.drawable.putingbato_1};
    private int museodabawenyo[] = new int[]{R.drawable.museodabawenyo_0,R.drawable.museodabawenyo_1};
    private int osmenapark[] = new int[]{R.drawable.osmenapark_0,R.drawable.osmenapark_1};
    private int panas[] = new int[]{R.drawable.panas_0,R.drawable.panas_1};
    private int paradise[] = new int[]{R.drawable.paradise_0,R.drawable.paradise_1};
    private int ppark[] = new int[]{R.drawable.ppark_0,R.drawable.ppark_1};
    private int phileagle[] = new int[]{R.drawable.phileagle_0,R.drawable.phileagle_1};
    private int redemp[] = new int[]{R.drawable.redemp_0,R.drawable.redemp_1};
    private int roxasnightmarket[] = new int[]{R.drawable.roxasnightmarket_0,R.drawable.roxasnightmarket_1};
    private int watersports[] = new int[]{R.drawable.samalwatersports_0,R.drawable.samalwatersports_1};
    private int sanpedro[] = new int[]{R.drawable.sanpedro_0,R.drawable.sanpedro_1};
    private int seawall[] = new int[]{R.drawable.seawall_0,R.drawable.seawall_1};
    private int shrine[] = new int[]{R.drawable.shrine_0,R.drawable.shrine_1};
    private int smdavao[] = new int[]{R.drawable.smdavao_0,R.drawable.smdavao_1};
    private int smlanang[] = new int[]{R.drawable.smlanang_0,R.drawable.smlanang_1};
    private int staana[] = new int[]{R.drawable.staana_0,R.drawable.staana_1};
    private int sulorchids[] = new int[]{R.drawable.sulorchids_0,R.drawable.sulorchids_1};
    private int tagbaobo[] = new int[]{R.drawable.tagbaobo_0,R.drawable.tagbaobo_1};
    private int talomo[] = new int[]{R.drawable.talomobeachresort_0,R.drawable.talomobeachresort_1};
    private int tamayong[] = new int[]{R.drawable.tamayong_0,R.drawable.tamayong_1};
    private int tribu[] = new int[]{R.drawable.tribu_0,R.drawable.tribu_1};
    private int vanishing[] = new int[]{R.drawable.vanishing_0,R.drawable.vanishing_1};
    private int zipcity[] = new int[]{R.drawable.zipcity_0,R.drawable.zipcity_1};
    private int zorb[] = new int[]{R.drawable.zorb_0,R.drawable.zorb_1};
    //agoda
    private int balibali[] = new int[]{R.drawable.balibali_0,R.drawable.balibali_1};
    private int campHoliday[] = new int[]{R.drawable.campholiday_0,R.drawable.campholiday_1};
    private int chemas[] = new int[]{R.drawable.chemasbythesea_0,R.drawable.chemasbythesea_1};
    private int crown[] = new int[]{R.drawable.crownregency_0,R.drawable.crownregency_1};
    private int grandmenseng[] = new int[]{R.drawable.grandmenseng_0,R.drawable.grandmenseng_1};
    private int grandregal[] = new int[]{R.drawable.grandregal_0,R.drawable.grandregal_1};
    private int hofGorei[] = new int[]{R.drawable.hofgorei_0,R.drawable.hofgorei_1};
    private int paradiseIsland[] = new int[]{R.drawable.paradiseisland_0,R.drawable.paradiseisland_1};
    private int parkInn[] = new int[]{R.drawable.parkinn_0,R.drawable.parkinn_1};
    private int puntadelsol[] = new int[]{R.drawable.puntadelsol_0,R.drawable.puntadelsol_1};
    private int redknights[] = new int[]{R.drawable.redknights_0,R.drawable.redknights_1};
    private int redplanet[] = new int[]{R.drawable.redplanet_0,R.drawable.redplanet_1};
    private int seda[] = new int[]{R.drawable.sedaabreeza_0,R.drawable.sedaabreeza_1};
    private int apoview[] = new int[]{R.drawable.theapoview_0,R.drawable.theapoview_1};
    private int marcopolo[] = new int[]{R.drawable.themarcopolo_0,R.drawable.themarcopolo_1};
    private int pinnacle[] = new int[]{R.drawable.thepinnacle_0,R.drawable.thepinnacle_1};
    private int royalmandaya[] = new int[]{R.drawable.theroyalmandaya_0,R.drawable.theroyalmandaya_1};
    private int strandhotel[] = new int[]{R.drawable.thestrandhotel_0,R.drawable.thestrandhotel_1};
    private int urbanlivingzen[] = new int[]{R.drawable.urbanlivingzen_0,R.drawable.urbanlivingzen_1};
    private int waterfront[] = new int[]{R.drawable.waterfront_0,R.drawable.waterfront_1};

    String title;
    TextView tvTitle;
    private Activity activity;

    ImagesController(Context context) {
        this.mContext = context;
        this.activity = (Activity) context;
    }

    public void renderImages(Marker marker, View view, TextView theTitle){
        title = marker.getTitle();
        tvTitle = theTitle;
        ImageButton imageButton1 = view.findViewById(R.id.imageButton1);
        ImageButton imageButton2 = view.findViewById(R.id.imageButton2);
        long duration = 500;

        imageButton1.setOnHoverListener((view1, motionEvent) -> {
            ZoomAnimation zoomAnimation = new ZoomAnimation(activity);
            zoomAnimation.zoom(view1, duration);
            return false;
        });

        imageButton2.setOnHoverListener((view12, motionEvent) -> {
            ZoomAnimation zoomAnimation = new ZoomAnimation(activity);
            zoomAnimation.zoom(view12, duration);
            return false;
        });
        //tripadvisor
        if (title.contains("Abreeza")){
            imageButton1.setImageResource(abreeza[0]);
            imageButton2.setImageResource(abreeza[1]);
        }
        if (title.contains("Aldevinco")){
            imageButton1.setImageResource(aldevinco[0]);
            imageButton2.setImageResource(aldevinco[1]);
        }
        if (title.contains("Bankerohan")){
            imageButton1.setImageResource(bankerohan[0]);
            imageButton2.setImageResource(bankerohan[1]);
        }
        if (title.contains("BEMWA")){
            imageButton1.setImageResource(bemwa[0]);
            imageButton2.setImageResource(bemwa[1]);
        }
        if (title.contains("BlueJaz")){
            imageButton1.setImageResource(bluejaz[0]);
            imageButton2.setImageResource(bluejaz[1]);
        }
        if (title.contains("Butterfly")){
            imageButton1.setImageResource(butterfly[0]);
            imageButton2.setImageResource(butterfly[1]);
        }
        if (title.contains("Christina")){
            imageButton1.setImageResource(christina[0]);
            imageButton2.setImageResource(christina[1]);
        }
        if (title.contains("Coral")){
            imageButton1.setImageResource(coralgarden[0]);
            imageButton2.setImageResource(coralgarden[1]);
        }
        if (title.contains("Crocodile")){
            imageButton1.setImageResource(crocodilepark[0]);
            imageButton2.setImageResource(crocodilepark[1]);
        }
        if (title.contains("Davao Museum")){
            imageButton1.setImageResource(davaomuseum[0]);
            imageButton2.setImageResource(davaomuseum[1]);
        }
        if (title.contains("D'Bone")){
            imageButton1.setImageResource(dbone[0]);
            imageButton2.setImageResource(dbone[1]);
        }
        if (title.contains("Eden")){
            imageButton1.setImageResource(eden[0]);
            imageButton2.setImageResource(eden[1]);
        }
        if (title.contains("Felcris")){
            imageButton1.setImageResource(felcris[0]);
            imageButton2.setImageResource(felcris[1]);
        }
        if (title.contains("GAP")){
            imageButton1.setImageResource(gap[0]);
            imageButton2.setImageResource(gap[1]);
        }
        if (title.contains("Giant")){
            imageButton1.setImageResource(giant[0]);
            imageButton2.setImageResource(giant[1]);
        }
        if (title.contains("The Peak")){
            imageButton1.setImageResource(gmallthepeak[0]);
            imageButton2.setImageResource(gmallthepeak[1]);
        }
        if (title.equalsIgnoreCase("GMALL")){
            imageButton1.setImageResource(gmall[0]);
            imageButton2.setImageResource(gmall[1]);
        }
        if (title.contains("Golden")){
            imageButton1.setImageResource(golden[0]);
            imageButton2.setImageResource(golden[1]);
        }
        if (title.contains("Gumamela")){
            imageButton1.setImageResource(gumamela[0]);
            imageButton2.setImageResource(gumamela[1]);
        }
        if (title.contains("Hagimit")){
            imageButton1.setImageResource(hagimit[0]);
            imageButton2.setImageResource(hagimit[1]);
        }
        if (title.contains("Japanese Tunnel")){
            imageButton1.setImageResource(japtunnel[0]);
            imageButton2.setImageResource(japtunnel[1]);
        }
        if (title.contains("Japanese Cave House")){
            imageButton1.setImageResource(japcavehouse[0]);
            imageButton2.setImageResource(japcavehouse[1]);
        }
        if (title.contains("Kaputian")){
            imageButton1.setImageResource(kaputian[0]);
            imageButton2.setImageResource(kaputian[1]);
        }
        if (title.contains("Lolengs")){
            imageButton1.setImageResource(lolengs[0]);
            imageButton2.setImageResource(lolengs[1]);
        }
        if (title.contains("Lon Hua")){
            imageButton1.setImageResource(lonhua[0]);
            imageButton2.setImageResource(lonhua[1]);
        }
        if (title.contains("Magsaysay")){
            imageButton1.setImageResource(magsaysay[0]);
            imageButton2.setImageResource(magsaysay[1]);
        }
        if (title.contains("Malagos Garden")){
            imageButton1.setImageResource(malagos[0]);
            imageButton2.setImageResource(malagos[1]);
        }
        if (title.contains("Matina Town Square")){
            imageButton1.setImageResource(mts[0]);
            imageButton2.setImageResource(mts[1]);
        }
        if (title.contains("Maxima")){
            imageButton1.setImageResource(maxima[0]);
            imageButton2.setImageResource(maxima[1]);
        }
        if (title.contains("Monfort")){
            imageButton1.setImageResource(monfort[0]);
            imageButton2.setImageResource(monfort[1]);
        }
        if (title.contains("Mount Apo")){
            imageButton1.setImageResource(mtapo[0]);
            imageButton2.setImageResource(mtapo[1]);
        }
        if (title.contains("Galintan")){
            imageButton1.setImageResource(galintan[0]);
            imageButton2.setImageResource(galintan[1]);
        }
        if (title.contains("Puting Bato")){
            imageButton1.setImageResource(putingbato[0]);
            imageButton2.setImageResource(putingbato[1]);
        }
        if (title.contains("Museo Dabawenyo")){
            imageButton1.setImageResource(museodabawenyo[0]);
            imageButton2.setImageResource(museodabawenyo[1]);
        }
        if (title.contains("Osmena Park")){
            imageButton1.setImageResource(osmenapark[0]);
            imageButton2.setImageResource(osmenapark[1]);
        }
        if (title.contains("Panas")){
            imageButton1.setImageResource(panas[0]);
            imageButton2.setImageResource(panas[1]);
        }
        if (title.contains("Paradise")){
            imageButton1.setImageResource(paradise[0]);
            imageButton2.setImageResource(paradise[1]);
        }
        if (title.contains("Peoples Park")){
            imageButton1.setImageResource(ppark[0]);
            imageButton2.setImageResource(ppark[1]);
        }
        if (title.contains("Eagle")){
            imageButton1.setImageResource(phileagle[0]);
            imageButton2.setImageResource(phileagle[1]);
        }
        if (title.contains("Redemptorist Church")){
            imageButton1.setImageResource(redemp[0]);
            imageButton2.setImageResource(redemp[1]);
        }
        if (title.contains("Roxas Avenue Night Market")){
            imageButton1.setImageResource(roxasnightmarket[0]);
            imageButton2.setImageResource(roxasnightmarket[1]);
        }
        if (title.contains("Samal Watersports Center")){
            imageButton1.setImageResource(watersports[0]);
            imageButton2.setImageResource(watersports[1]);
        }
        if (title.contains("San Pedro Cathedral")){
            imageButton1.setImageResource(sanpedro[0]);
            imageButton2.setImageResource(sanpedro[1]);
        }
        if (title.contains("Seawall")){
            imageButton1.setImageResource(seawall[0]);
            imageButton2.setImageResource(seawall[1]);
        }
        if (title.contains("Shrine of the Holy Infant Jesus of Prague")){
            imageButton1.setImageResource(shrine[0]);
            imageButton2.setImageResource(shrine[1]);
        }
        if (title.contains("SM City Davao")){
            imageButton1.setImageResource(smdavao[0]);
            imageButton2.setImageResource(smdavao[1]);
        }
        if (title.contains("SM Lanang Premiere")){
            imageButton1.setImageResource(smlanang[0]);
            imageButton2.setImageResource(smlanang[1]);
        }
        if (title.contains("Sta Ana Shrine")){
            imageButton1.setImageResource(staana[0]);
            imageButton2.setImageResource(staana[1]);
        }
        if (title.contains("Sul Orchids Palma Gil")){
            imageButton1.setImageResource(sulorchids[0]);
            imageButton2.setImageResource(sulorchids[1]);
        }
        if (title.contains("Tagbaobo Falls")){
            imageButton1.setImageResource(tagbaobo[0]);
            imageButton2.setImageResource(tagbaobo[1]);
        }
        if (title.contains("Talomo Beach Resort")){
            imageButton1.setImageResource(talomo[0]);
            imageButton2.setImageResource(talomo[1]);
        }
        if (title.contains("Tamayong Prayer Mountain")){
            imageButton1.setImageResource(tamayong[0]);
            imageButton2.setImageResource(tamayong[1]);
        }
        if (title.contains("Tribu K Mindanawan Cultural Village")){
            imageButton1.setImageResource(tribu[0]);
            imageButton2.setImageResource(tribu[1]);
        }
        if (title.contains("Vanishing Island")){
            imageButton1.setImageResource(vanishing[0]);
            imageButton2.setImageResource(vanishing[1]);
        }
        if (title.contains("Zip City")){
            imageButton1.setImageResource(zipcity[0]);
            imageButton2.setImageResource(zipcity[1]);
        }
        if (title.contains("Zorb")){
            imageButton1.setImageResource(zorb[0]);
            imageButton2.setImageResource(zorb[1]);
        }
        //Agoda
        if (title.contains("Bali Bali Beach Resort")){
            imageButton1.setImageResource(balibali[0]);
            imageButton2.setImageResource(balibali[1]);
        }
        if (title.contains("Camp Holiday Resort")){
            imageButton1.setImageResource(campHoliday[0]);
            imageButton2.setImageResource(campHoliday[1]);
        }
        if (title.contains("Chemas by the Sea Resort")){
            imageButton1.setImageResource(chemas[0]);
            imageButton2.setImageResource(chemas[1]);
        }
        if (title.contains("Crown Regency Residences")){
            imageButton1.setImageResource(crown[0]);
            imageButton2.setImageResource(crown[1]);
        }
        if (title.contains("Grand Men Seng Hotel")){
            imageButton1.setImageResource(grandmenseng[0]);
            imageButton2.setImageResource(grandmenseng[1]);
        }
        if (title.contains("Grand Regal Hotel")){
            imageButton1.setImageResource(grandregal[0]);
            imageButton2.setImageResource(grandregal[1]);
        }
        if (title.contains("Hof Gorei Beach Resort")){
            imageButton1.setImageResource(hofGorei[0]);
            imageButton2.setImageResource(hofGorei[1]);
        }
        if (title.contains("Paradise Island Park & Beach Resort")){
            imageButton1.setImageResource(paradiseIsland[0]);
            imageButton2.setImageResource(paradiseIsland[1]);
        }
        if (title.contains("Park Inn by Radisson")){
            imageButton1.setImageResource(parkInn[0]);
            imageButton2.setImageResource(parkInn[1]);
        }
        if (title.contains("Punta Del Sol Beach Resort")){
            imageButton1.setImageResource(puntadelsol[0]);
            imageButton2.setImageResource(puntadelsol[1]);
        }
        if (title.contains("Red Knight Gardens Apartelle")){
            imageButton1.setImageResource(redknights[0]);
            imageButton2.setImageResource(redknights[1]);
        }
        if (title.contains("Red Planet")){
            imageButton1.setImageResource(redplanet[0]);
            imageButton2.setImageResource(redplanet[1]);
        }
        if (title.contains("Seda Abreeza Davao")){
            imageButton1.setImageResource(seda[0]);
            imageButton2.setImageResource(seda[1]);
        }
        if (title.contains("The Apo View Hotel")){
            imageButton1.setImageResource(apoview[0]);
            imageButton2.setImageResource(apoview[1]);
        }
        if (title.contains("The Marco Polo Hotel")){
            imageButton1.setImageResource(marcopolo[0]);
            imageButton2.setImageResource(marcopolo[1]);
        }
        if (title.contains("The Pinnacle Hotel and Suites")){
            imageButton1.setImageResource(pinnacle[0]);
            imageButton2.setImageResource(pinnacle[1]);
        }
        if (title.contains("The Royal Mandaya Hotel")){
            imageButton1.setImageResource(royalmandaya[0]);
            imageButton2.setImageResource(royalmandaya[1]);
        }
        if (title.contains("The Strand Suites and Dormitel")){
            imageButton1.setImageResource(strandhotel[0]);
            imageButton2.setImageResource(strandhotel[1]);
        }
        if (title.contains("Urban Living Zen Hotel")){
            imageButton1.setImageResource(urbanlivingzen[0]);
            imageButton2.setImageResource(urbanlivingzen[1]);
        }
        if (title.contains("Waterfront Insular Hotel")){
            imageButton1.setImageResource(waterfront[0]);
            imageButton2.setImageResource(waterfront[1]);
        }

    }
}

