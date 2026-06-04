package laki.webide.core;

import android.content.Context;
import java.util.HashMap;

public class ScDefine {
    public static final String[] CIDS = new String[]{"101", "109", "111", "110", "112", "105", "113", "102", "104", "114", "103", "106", "107", "108", "600"};
    public static final String CID_BASEBALL = "103";
    public static final String CID_CALC = "104";
    public static final String CID_CUSTOM_EDIT = "600";
    public static final String CID_DICE = "113";
    public static final String CID_FAMILY_CALL = "110";
    public static final String CID_GET_TIME = "109";
    public static final String CID_HYBRIDWEB = "106";
    public static final String CID_LOTTO = "114";
    public static final String CID_MEMO = "105";
    public static final String CID_PAIRPIC = "108";
    public static final String CID_RPS = "102";
    public static final String CID_SPEEDCLICK = "107";
    public static final String CID_TOAST = "101";
    public static final String CID_WEB_BROWSER = "111";
    public static final String CID_WHOAMI = "112";
    public static final boolean FLAG_NOT_STUDYING = false;
    public static final boolean FLAG_STUDYING = true;
    public static final int PROJECT_TYPE_DOWNLOAD = 2;
    public static final int PROJECT_TYPE_GENERAL = 0;
    public static final int PROJECT_TYPE_HINT = 1;
  /*  public static final int[][] scBaseBallRes = new int[][]{{R.drawable.sc_baseball_1, 300}, {R.drawable.sc_baseball_2, 300}, {R.drawable.sc_baseball_3, 500}, {R.drawable.sc_baseball_4, 300}, {R.drawable.sc_baseball_5, 500}, {R.drawable.sc_baseball_6, 300}, {R.drawable.sc_baseball_7, 500}, {R.drawable.sc_baseball_8, 300}, {R.drawable.sc_baseball_9, 300}, {R.drawable.sc_baseball_10, 700}};
    public static final int[][] scCalcRes = new int[][]{{R.drawable.sc_calculator_1, 500}, {R.drawable.sc_calculator_2, 500}, {R.drawable.sc_calculator_3, 500}, {R.drawable.sc_calculator_4, 500}, {R.drawable.sc_calculator_5, 500}, {R.drawable.sc_calculator_6, 500}, {R.drawable.sc_calculator_7, 500}};
    public static final int[][] scDiceRes = new int[][]{{R.drawable.sc_dice_1, 300}, {R.drawable.sc_dice_2, 300}, {R.drawable.sc_dice_3, 500}, {R.drawable.sc_dice_4, 300}, {R.drawable.sc_dice_5, 500}};
    public static final int[][] scFamilyCallRes = new int[][]{{R.drawable.sc_famcall_1, 300}, {R.drawable.sc_famcall_2, 500}, {R.drawable.sc_famcall_3, 700}, {R.drawable.sc_famcall_4, 500}, {R.drawable.sc_famcall_5, 700}};
    public static final int[][] scGetTimeRes = new int[][]{{R.drawable.sc_gettime_1, 300}, {R.drawable.sc_gettime_2, 300}, {R.drawable.sc_gettime_3, 500}, {R.drawable.sc_gettime_4, 300}, {R.drawable.sc_gettime_5, 500}};
    public static final int[][] scHybridRes = new int[][]{{R.drawable.sc_hybrid_1, 500}, {R.drawable.sc_hybrid_2, 500}, {R.drawable.sc_hybrid_3, 500}, {R.drawable.sc_hybrid_4, 500}, {R.drawable.sc_hybrid_5, 500}, {R.drawable.sc_hybrid_6, 700}};
    public static final int[][] scLottoRes = new int[][]{{R.drawable.sc_lotto_1, 300}, {R.drawable.sc_lotto_2, 500}, {R.drawable.sc_lotto_3, 300}, {R.drawable.sc_lotto_4, 300}, {R.drawable.sc_lotto_5, 300}, {R.drawable.sc_lotto_6, 300}, {R.drawable.sc_lotto_7, 300}, {R.drawable.sc_lotto_8, 300}, {R.drawable.sc_lotto_9, 500}};
    public static final int[][] scMemoRes = new int[][]{{R.drawable.sc_memo_1, 500}, {R.drawable.sc_memo_2, 500}, {R.drawable.sc_memo_3, 50}, {R.drawable.sc_memo_4, 50}, {R.drawable.sc_memo_5, 50}, {R.drawable.sc_memo_6, 50}, {R.drawable.sc_memo_7, 50}, {R.drawable.sc_memo_8, 50}, {R.drawable.sc_memo_9, 50}, {R.drawable.sc_memo_10, 700}};
    public static final int[][] scPairPicRes = new int[][]{{R.drawable.sc_memory_0, 300}, {R.drawable.sc_memory_1, 300}, {R.drawable.sc_memory_2, 300}, {R.drawable.sc_memory_3, 300}, {R.drawable.sc_memory_4, 300}, {R.drawable.sc_memory_5, 300}, {R.drawable.sc_memory_6, 300}, {R.drawable.sc_memory_7, 300}, {R.drawable.sc_memory_8, 300}, {R.drawable.sc_memory_9, 300}, {R.drawable.sc_memory_10, 300}};
    public static final int[][] scRpsRes = new int[][]{{R.drawable.sc_rps_1, 500}, {R.drawable.sc_rps_2, 500}, {R.drawable.sc_rps_3, 500}, {R.drawable.sc_rps_4, 500}};
    public static final int[][] scSpeedClickRes = new int[][]{{R.drawable.sc_speed_1, 300}, {R.drawable.sc_speed_2, 300}, {R.drawable.sc_speed_3, 300}, {R.drawable.sc_speed_4, 300}, {R.drawable.sc_speed_5, 300}, {R.drawable.sc_speed_6, 300}, {R.drawable.sc_speed_7, 300}, {R.drawable.sc_speed_8, 300}, {R.drawable.sc_speed_9, 300}, {R.drawable.sc_speed_10, 300}, {R.drawable.sc_speed_11, 600}};
    public static final int[][] scToastRes = new int[][]{{R.drawable.sc_toast_1, 50}, {R.drawable.sc_toast_2, 50}, {R.drawable.sc_toast_3, 50}, {R.drawable.sc_toast_4, 50}, {R.drawable.sc_toast_5, 50}, {R.drawable.sc_toast_6, 50}, {R.drawable.sc_toast_7, 500}, {R.drawable.sc_toast_8, 500}, {R.drawable.sc_toast_9, 500}, {R.drawable.sc_toast_10, 500}};
    public static final int[][] scWebBrowserRes = new int[][]{{R.drawable.sc_web_1, 300}, {R.drawable.sc_web_2, 300}, {R.drawable.sc_web_3, 300}, {R.drawable.sc_web_4, 500}};
    public static final int[][] scWhoamiRes = new int[][]{{R.drawable.sc_whoami_1, 300}, {R.drawable.sc_whoami_2, 300}, {R.drawable.sc_whoami_3, 500}, {R.drawable.sc_whoami_4, 500}, {R.drawable.sc_whoami_5, 300}, {R.drawable.sc_whoami_6, 500}, {R.drawable.sc_whoami_7, 500}, {R.drawable.sc_whoami_8, 500}};
*/
    public static boolean isCustomEditMode(String var0) {
        return Integer.valueOf(var0).intValue() >= Integer.valueOf("600").intValue();
    }

  /*  public static HashMap makeBaseball(Context var0, boolean var1, int var2) {
        HashMap var3 = new HashMap();
        var3.put("sc_id", "103");
        var3.put("sc_name", "BaseBall - BullsAndCows");
        var3.put("sc_desc", "Create a Baseball - Bulls and Cows game");
        var3.put("sc_app_icon", "app_icon03");
        var3.put("sc_desc_res", scBaseBallRes);
        var3.put("sc_desc_bg", Integer.valueOf(R.drawable.sc_baseball_bg));
        var3.put("sc_feature", "Select a random 3-digit number\nDigits must be all different\nYou can delete one digit at a time\nDecide strike(Bulls) or ball(Cows)\n    * strike(Bulls) if you guess the right digit in right position\n    * ball(Cows) if the digit you guessed in different positions\n    * example : Random Number - 123, Your Guess: 132\n    * output : 1 strike, 2 ball\nRecord your attempts\nContinue until you guess the correct number");
        var3.put("sc_market_url", "com.sketchware.dobaseball");
        var3.put("sc_pkg_name", "com.my.dobaseball");
        var3.put("sc_ws_name", "Baseball");
        var3.put("sc_tag", "# Character | Duplicate");
        var3.put("sc_price_type", "FREE");
        var3.put("sc_studying", Boolean.valueOf(var1));
        var3.put("sc_pre_step", "112;114");
        var3.put("sc_view_sort", Integer.valueOf(var2));
        return var3;
    }

    public static HashMap makeCalc(Context var0, boolean var1, int var2) {
        HashMap var3 = new HashMap();
        var3.put("sc_id", "104");
        var3.put("sc_name", "Calculator");
        var3.put("sc_desc", "Create a simple calculator");
        var3.put("sc_app_icon", "app_icon04");
        var3.put("sc_desc_res", scCalcRes);
        var3.put("sc_desc_bg", Integer.valueOf(R.drawable.sc_calculator_bg));
        var3.put("sc_feature", "Arithmetic operations (+, -, x, /)\nDon't allow divisions by zero\n");
        var3.put("sc_market_url", "com.sketchware.docalc");
        var3.put("sc_pkg_name", "com.my.docalc");
        var3.put("sc_ws_name", "Calculator");
        var3.put("sc_tag", "# Arithmetic operation | Data types");
        var3.put("sc_price_type", "FREE");
        var3.put("sc_studying", Boolean.valueOf(var1));
        var3.put("sc_pre_step", "112");
        var3.put("sc_view_sort", Integer.valueOf(var2));
        return var3;
    }

    public static HashMap makeDice(Context var0, boolean var1, int var2) {
        HashMap var3 = new HashMap();
        var3.put("sc_id", "113");
        var3.put("sc_name", "Dice");
        var3.put("sc_desc", "Create a simple dice application");
        var3.put("sc_app_icon", "app_icon13");
        var3.put("sc_desc_res", scDiceRes);
        var3.put("sc_desc_bg", Integer.valueOf(R.drawable.sc_dice_bg));
        var3.put("sc_feature", "Generate a random number between 1 and 6\n");
        var3.put("sc_market_url", "com.sketchware.dodice");
        var3.put("sc_pkg_name", "com.my.dodice");
        var3.put("sc_ws_name", "Dice");
        var3.put("sc_tag", "# Random | Image");
        var3.put("sc_price_type", "FREE");
        var3.put("sc_studying", Boolean.valueOf(var1));
        var3.put("sc_pre_step", "112");
        var3.put("sc_view_sort", Integer.valueOf(var2));
        return var3;
    }

    public static HashMap makeFamilyCall(Context var0, boolean var1, int var2) {
        HashMap var3 = new HashMap();
        var3.put("sc_id", "110");
        var3.put("sc_name", "Family call");
        var3.put("sc_desc", "Create a phone call application");
        var3.put("sc_app_icon", "app_icon10");
        var3.put("sc_desc_res", scFamilyCallRes);
        var3.put("sc_desc_bg", Integer.valueOf(R.drawable.sc_famcall_bg));
        var3.put("sc_feature", "Implement calling function\nOnly allow integers\n");
        var3.put("sc_market_url", "com.sketchware.docall");
        var3.put("sc_pkg_name", "com.my.docall");
        var3.put("sc_ws_name", "FamilyCall");
        var3.put("sc_tag", "# Intent | Calling");
        var3.put("sc_price_type", "FREE");
        var3.put("sc_studying", Boolean.valueOf(var1));
        var3.put("sc_pre_step", "");
        var3.put("sc_view_sort", Integer.valueOf(var2));
        return var3;
    }

    public static HashMap makeGetTime(Context var0, boolean var1, int var2) {
        HashMap var3 = new HashMap();
        var3.put("sc_id", "109");
        var3.put("sc_name", "GetTime");
        var3.put("sc_desc", "Create an application that gets current time");
        var3.put("sc_app_icon", "app_icon09");
        var3.put("sc_desc_res", scGetTimeRes);
        var3.put("sc_desc_bg", Integer.valueOf(R.drawable.sc_gettime_bg));
        var3.put("sc_feature", "Add a calendar component\nSet date format\n");
        var3.put("sc_market_url", "com.sketchware.dogettime");
        var3.put("sc_pkg_name", "com.my.dogettime");
        var3.put("sc_ws_name", "GetTime");
        var3.put("sc_tag", "# Widget | Time");
        var3.put("sc_price_type", "FREE");
        var3.put("sc_studying", Boolean.valueOf(var1));
        var3.put("sc_pre_step", "");
        var3.put("sc_view_sort", Integer.valueOf(var2));
        return var3;
    }

    public static HashMap makeLotto(Context var0, boolean var1, int var2) {
        HashMap var3 = new HashMap();
        var3.put("sc_id", "114");
        var3.put("sc_name", "Lotto");
        var3.put("sc_desc", "Create a Lotto simulator");
        var3.put("sc_app_icon", "app_icon14");
        var3.put("sc_desc_res", scLottoRes);
        var3.put("sc_desc_bg", Integer.valueOf(R.drawable.sc_lotto_bg));
        var3.put("sc_feature", "Generate a random number between 1 and 45 each time a button is clicked\nDuplicate numbers are not allowed\n");
        var3.put("sc_market_url", "com.sketchware.dolotto");
        var3.put("sc_pkg_name", "com.my.dolotto");
        var3.put("sc_ws_name", "Lotto");
        var3.put("sc_tag", "# Random | Duplicate");
        var3.put("sc_price_type", "FREE");
        var3.put("sc_studying", Boolean.valueOf(var1));
        var3.put("sc_pre_step", "113;102");
        var3.put("sc_view_sort", Integer.valueOf(var2));
        return var3;
    }

    public static HashMap makeMemo(Context var0, boolean var1, int var2) {
        HashMap var3 = new HashMap();
        var3.put("sc_id", "105");
        var3.put("sc_name", "Memo");
        var3.put("sc_desc", "Create a memo application");
        var3.put("sc_app_icon", "app_icon05");
        var3.put("sc_desc_res", scMemoRes);
        var3.put("sc_desc_bg", Integer.valueOf(R.drawable.sc_memo_bg));
        var3.put("sc_feature", "Save title and message\nShow the saved list of memos\nEdit the saved memo\nDelete the saved memo\nStart a new activity for Memo Editor\n");
        var3.put("sc_market_url", "com.sketchware.domemo");
        var3.put("sc_pkg_name", "com.my.domemo");
        var3.put("sc_ws_name", "Memo");
        var3.put("sc_tag", "# File | Activity");
        var3.put("sc_price_type", "FREE");
        var3.put("sc_studying", Boolean.valueOf(var1));
        var3.put("sc_pre_step", "109;110");
        var3.put("sc_view_sort", Integer.valueOf(var2));
        return var3;
    }

    public static HashMap makePairPic(Context var0, boolean var1, int var2) {
        HashMap var3 = new HashMap();
        var3.put("sc_id", "108");
        var3.put("sc_name", "Match pairs");
        var3.put("sc_desc", "Create a match pairs memory game");
        var3.put("sc_app_icon", "app_icon08");
        var3.put("sc_desc_res", scPairPicRes);
        var3.put("sc_desc_bg", Integer.valueOf(R.drawable.sc_memory_bg));
        var3.put("sc_feature", "Place pictures in random positions\nDisplay hidden images on start\nIf two selected pictures are the same, then display them; otherwise, hide them\nStop when all pictures are displayed\n");
        var3.put("sc_market_url", "com.sketchware.dopairpic");
        var3.put("sc_pkg_name", "com.my.dopairpic");
        var3.put("sc_ws_name", "Pairpic");
        var3.put("sc_tag", "# Random | Duplicate | Image");
        var3.put("sc_price_type", "FREE");
        var3.put("sc_studying", Boolean.valueOf(var1));
        var3.put("sc_pre_step", "114;107");
        var3.put("sc_view_sort", Integer.valueOf(var2));
        return var3;
    }

    public static HashMap makeRps(Context var0, boolean var1, int var2) {
        HashMap var3 = new HashMap();
        var3.put("sc_id", "102");
        var3.put("sc_name", "Rock.Paper.Scissors");
        var3.put("sc_desc", "Create a rock.paper.scissors game");
        var3.put("sc_app_icon", "app_icon02");
        var3.put("sc_desc_res", scRpsRes);
        var3.put("sc_desc_bg", Integer.valueOf(R.drawable.sc_rps_bg));
        var3.put("sc_feature", "Select rock, paper, or scissors\nA.I. also chooses rock, paper, or scissors\nCompare and decide who won");
        var3.put("sc_market_url", "com.sketchware.dorps");
        var3.put("sc_pkg_name", "com.my.dorps");
        var3.put("sc_ws_name", "RPS");
        var3.put("sc_tag", "# Comparison | Random");
        var3.put("sc_price_type", "FREE");
        var3.put("sc_studying", Boolean.valueOf(var1));
        var3.put("sc_pre_step", "112;113");
        var3.put("sc_view_sort", Integer.valueOf(var2));
        return var3;
    }

    public static HashMap makeSpeedClick(Context var0, boolean var1, int var2) {
        HashMap var3 = new HashMap();
        var3.put("sc_id", "107");
        var3.put("sc_name", "Clicking speed test");
        var3.put("sc_desc", "Create a clicking speed test application");
        var3.put("sc_app_icon", "app_icon07");
        var3.put("sc_desc_res", scSpeedClickRes);
        var3.put("sc_desc_bg", Integer.valueOf(R.drawable.sc_speed_bg));
        var3.put("sc_feature", "Place random numbers\nCheck the clicking sequence\nDisable clicked numbers\nShow the time taken");
        var3.put("sc_market_url", "com.sketchware.dospeedclick");
        var3.put("sc_pkg_name", "com.my.dospeedclick");
        var3.put("sc_ws_name", "Speedclick");
        var3.put("sc_tag", "# Random | Duplicate | Sequence");
        var3.put("sc_price_type", "FREE");
        var3.put("sc_studying", Boolean.valueOf(var1));
        var3.put("sc_pre_step", "102;114");
        var3.put("sc_view_sort", Integer.valueOf(var2));
        return var3;
    }

    public static HashMap makeToast(Context var0, boolean var1, int var2) {
        HashMap var3 = new HashMap();
        var3.put("sc_id", "101");
        var3.put("sc_name", "Message box");
        var3.put("sc_desc", "Create an application that outputs an input string");
        var3.put("sc_app_icon", "app_icon01");
        var3.put("sc_desc_res", scToastRes);
        var3.put("sc_desc_bg", Integer.valueOf(R.drawable.sc_toast_bg));
        var3.put("sc_feature", "Use a widget that takes a user input\nAdd a function to a button\nShow a message box (Toast)");
        var3.put("sc_market_url", "com.sketchware.dotoast");
        var3.put("sc_pkg_name", "com.my.dotoast");
        var3.put("sc_ws_name", "Toast");
        var3.put("sc_tag", "# Widget | Event");
        var3.put("sc_price_type", "FREE");
        var3.put("sc_studying", Boolean.valueOf(var1));
        var3.put("sc_pre_step", "");
        var3.put("sc_view_sort", Integer.valueOf(var2));
        return var3;
    }

    public static HashMap makeWebBrowser(Context var0, boolean var1, int var2) {
        HashMap var3 = new HashMap();
        var3.put("sc_id", "111");
        var3.put("sc_name", "Web broswer");
        var3.put("sc_desc", "Create a web browser application");
        var3.put("sc_app_icon", "app_icon11");
        var3.put("sc_desc_res", scWebBrowserRes);
        var3.put("sc_desc_bg", Integer.valueOf(R.drawable.sc_web_bg));
        var3.put("sc_feature", "Take an input URL string\nOpen the browser using Intent\n");
        var3.put("sc_market_url", "com.sketchware.doweb");
        var3.put("sc_pkg_name", "com.my.doweb");
        var3.put("sc_ws_name", "WebBrowser");
        var3.put("sc_tag", "# Intent | URL");
        var3.put("sc_price_type", "FREE");
        var3.put("sc_studying", Boolean.valueOf(var1));
        var3.put("sc_pre_step", "");
        var3.put("sc_view_sort", Integer.valueOf(var2));
        return var3;
    }

    public static HashMap makeWhoami(Context var0, boolean var1, int var2) {
        HashMap var3 = new HashMap();
        var3.put("sc_id", "112");
        var3.put("sc_name", "Who am I ?");
        var3.put("sc_desc", "Create a guessing game");
        var3.put("sc_app_icon", "app_icon12");
        var3.put("sc_desc_res", scWhoamiRes);
        var3.put("sc_desc_bg", Integer.valueOf(R.drawable.sc_whoami_bg));
        var3.put("sc_feature", "Generate a random number between 0 to 9\nCompare the input number to the random number\nNotify the input number is bigger or less than the random number\n");
        var3.put("sc_market_url", "com.sketchware.dowhoami");
        var3.put("sc_pkg_name", "com.my.dowhoami");
        var3.put("sc_ws_name", "Whoami");
        var3.put("sc_tag", "# Random | Comparison");
        var3.put("sc_price_type", "FREE");
        var3.put("sc_studying", Boolean.valueOf(var1));
        var3.put("sc_pre_step", "");
        var3.put("sc_view_sort", Integer.valueOf(var2));
        return var3;
    }*/
}

