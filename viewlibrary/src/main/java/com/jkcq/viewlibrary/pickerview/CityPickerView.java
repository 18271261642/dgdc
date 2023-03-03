package com.jkcq.viewlibrary.pickerview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.jkcq.viewlibrary.R;
import com.jkcq.viewlibrary.pickerview.adapter.ArrayWheelAdapter;
import com.jkcq.viewlibrary.pickerview.listener.OnItemSelectedListener;
import com.jkcq.viewlibrary.pickerview.view.WheelView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/*
 *
 * classes : com.jkcq.gym.view.pickerview
 * @author 苗恒聚
 * V 1.0.0
 * Create at 2016/10/22 16:46
 */
public class CityPickerView extends LinearLayout {


    private String[] provinces = new String[]{"北京市", "天津市", "上海市", "重庆市", "河北省", "山西省", "江苏省", "浙江省"
            , "安徽省", "福建省", "江西省", "山东省", "河南省", "湖北省", "湖南省", "广东省", "辽宁省", "吉林省", "黑龙江省", "广西壮族自治区"
            , "海南省", "台湾省", "四川省", "贵州省", "云南省", "西藏自治区", "陕西省", "甘肃省", "青海省", "宁夏回族自治区", "内蒙古自治区", "新疆维吾尔自治区", "香港特别行政区", "澳门特别行政区"

    };

    private String[] cityBeijing = new String[]{"东城区", "西城区", "海淀区", "朝阳区", "丰台区", "门头沟区", "石景山区", "房山区"
            , "通州区", "顺义区", "昌平区", "大兴区", "怀柔区", "平谷区", "延庆县", "密云县"
    };


    private String[] cityTianjin = new String[]{"和平区", "河东区", "河西区", "南开区",
            "河北区", "红桥区", "塘沽区", "汉沽区", "大港区", "东丽区", "西青区", "津南区", "北辰区", "武清区"
            , "宝坻区", "宁河县", "静海县", "蓟县"

    };

    private String[] cityShangHai = new String[]{"黄埔区", "卢湾区", "徐汇区", "长宁区"
            , "静安区", "普陀区", "闸北区", "虹口区", "杨浦区", "闵行区", "宝山区", "嘉定区", "浦东新区"
            , "金山区", "松江区", "青浦区", "南汇区", "奉贤区", "崇明县"
    };

    private String[] cityChongQing = new String[]{"万州区", "涪陵区", "渝中区", "大渡口区"
            , "江北区", "沙坪坝区", "九龙坡区", "南岸区", "北碚区", "万盛区", "双桥区", "渝北区", "巴南区"
            , "黔江区", "长寿区", "江津区", "合川区", "永川区", "南川区", "綦江县", "潼南县", "铜梁县", "大足县"
            , "荣昌县", "璧山县", "梁平县", "城口县", "丰都县", "垫江县", "武隆县", "忠县"
            , "开县", "云阳县", "奉节县", "巫山县", "巫溪县", "石柱土家族自治县", "秀山土家族苗族自治县", "酉阳土家族苗族自治县", "彭水苗族土家族自治县"
    };


    private String[] cityHebei = new String[]{"石家庄市", "唐山市"
            , "秦皇岛市", "邯郸市", "邢台市", "保定市", "张家口市", "承德市", "沧州市", "廊坊市", "衡水市"

    };


    private String[] cityShanXi = new String[]{"太原市", "大同市", "阳泉市", "长治市"
            , "晋城市", "朔州市", "晋中市", "运城市", "忻州市", "临汾市", "吕梁市"
    };


    private String[] cityJiangSu = new String[]{"南京市", "无锡市", "徐州市", "常州市"

            , "苏州市", "南通市", "连云港市", "淮安市", "盐城市", "扬州市", "镇江市", "泰州市", "宿迁市"


    };
    private String[] cityZheJiang = new String[]{"杭州市", "宁波市", "温州市"
            , "嘉兴市", "湖州市", "绍兴市", "金华市", "衢州市", "舟山市", "台州市", "丽水市"
    };

    private String[] cityAnHui = new String[]{"合肥市", "芜湖市", "蚌埠市"
            , "淮南市", "马鞍山市", "淮北市", "铜陵市", "安庆市", "黄山市", "滁州市", "阜阳市"
            , "宿州市", "巢湖市", "六安市", "毫州市", "池州市", "宣城市"
    };


    private String[] cityFuJian = new String[]{"福州市", "厦门市", "莆田市"
            , "三明市", "泉州市", "漳州市", "南平市", "龙岩市", "宁德市"
    };


    private String[] cityJiangXi = new String[]{"南昌市", "景德镇市", "萍乡市", "九江市", "新余市", "鹰潭市", "赣州市", "吉安市", "宜春市", "抚州市", "上饶市"

    };


    private String[] cityShanDong = new String[]{"济南市", "青岛市", "淄博市",
            "枣庄市", "东营市", "烟台市", "潍坊市", "济宁市", "泰安市", "威海市", "日照市", "莱芜市", "临沂市", "德州市", "聊城市", "滨州市", "菏泽市"

    };
    private String[] cityHeNan = new String[]{"郑州市", "开封市", "洛阳市", "平顶山市", "安阳市", "鹤壁市",
            "新乡市", "焦作市", "濮阳市", "许昌市", "漯河市", "三门峡市", "南阳市", "商丘市", "信阳市", "周口市", "驻马店市"

    };
    private String[] cityHuBei = new String[]{
            "武汉市", "黄石市", "十堰市", "宜昌市", "襄樊市", "鄂州市", "荆门市", "孝感市", "荆州市", "黄冈市", "咸宁市", "随州市", "神农架", "恩施土家族苗族自治州"
    };
    private String[] cityHuNan = new String[]{
            "长沙市", "株洲市", "湘潭市", "衡阳市", "邵阳市", "岳阳市", "常德市", "张家界市", "益阳市", "永州市", "怀化市", "娄底市", "郴州市", "湘西土家族苗族自治州"

    };
    private String[] cityGuangDong = new String[]{

            "广州市", "韶关市", "深圳市", "珠海市", "汕头市", "佛山市", "江门市", "湛江市", "茂名市", "肇庆市", "惠州市", "梅州市", "汕尾市", "河源市", "阳江市", "清远市", "东莞市", "中山市", "潮州市", "揭阳市", "云浮市"

    };
    private String[] cityLiaoNing = new String[]{

            "沈阳市", "大连市", "鞍山市", "抚顺市", "本溪市", "丹东市", "锦州市", "营口市", "阜新市", "辽阳市", "盘锦市", "铁岭市", "朝阳市", "葫芦岛市"

    };


    private String[] cityJiLin = new String[]{"长春市", "吉林市", "四平市", "辽源市", "通化市", "白山市", "松原市", "白城市", "延边朝鲜族自治区市"};
    private String[] cityHeilongjiang = new String[]{"哈尔滨市", "齐齐哈尔市", "鸡西市", "鹤岗市", "双鸭山市", "大庆市", "伊春市", "佳木斯市", "七台河市", "牡丹江市", "黑河市", "绥化市", "大兴安岭地区"};
    private String[] cityGuangXi = new String[]{"南宁市", "柳州市", "桂林市", "梧州市", "北海市", "防城港市", "钦州市", "贵港市", "玉林市", "百色市", "贺州市", "河池市", "来宾市", "崇左市"};
    private String[] cityHaiNan = new String[]{"海口市", "三亚市"};
    private String[] cityTaiWan = new String[]{"台北市", "台南市", "高雄市"};
    private String[] citySichuan = new String[]{"成都市", "自贡市", "攀枝花市", "泸州市", "德阳市", "绵阳市", "广元市", "遂宁市", "内江市", "乐山市", "南充市", "眉山市", "宜宾市", "广安市", "达州市", "雅安市"
            , "巴中市", "资阳市", "阿坝藏族羌族自治州", "甘孜藏族自治州", "凉山彝族自治州"};
    private String[] cityGuizhou = new String[]{"贵阳市", "六盘水市", "遵义市", "安顺市", "铜仁地区", "黔西南布依族苗族自治州", "毕节地区", "黔东南苗族侗族自治州", "黔南布依族苗族自治州"};
    private String[] cityYunNan = new String[]{"昆明市", "曲靖市", "玉溪市", "宝山市", "邵通市", "丽江市", "思茅市", "临沧市", "楚雄彝族自治州", "红河哈尼族彝族自治州", "文山壮族苗族自治州", "西双版纳傣族自治州", "大理白族自治州", "德宏傣族景颇族自治州", "怒江傈僳族自治州", "迪庆藏族自治州"};
    private String[] cityXiZang = new String[]{"拉萨市", "昌都地区", "山南地区", "日喀则地区", "那曲地区", "阿里地区", "林芝地区"};
    private String[] cityShangXi = new String[]{"西安市", "铜川市", "宝鸡市", "咸阳市", "渭南市", "延安市", "汉中市", "榆林市", "安康市", "商洛市"};
    private String[] cityGanSu = new String[]{"兰州市", "嘉峪关市", "金昌市", "白银市", "天水市", "武威市", "张掖市", "平凉市", "酒泉市", "庆阳市", "定西市", "陇南市", "临夏回族自治州", "甘南藏族自治州"};
    private String[] cityQinghai = new String[]{"西宁市", "海东地区", "海北藏族自治州", "黄南藏族自治州市", "海南藏族自治州", "果洛藏族自治州", "玉树藏族自治州", "海西蒙古族藏族自治州"};
    private String[] cityNingxia = new String[]{"银川市", "石嘴山市", "吴忠市", "固原市", "中卫市"};
    private String[] cityInnerMongolia = new String[]{"呼和浩特市", "包头市", "乌海市", "赤峰市", "通辽市", "鄂尔多斯市", "呼伦贝尔市", "巴彦卓尔市", "乌兰察布市", "兴安盟", "锡林郭勒盟", "阿拉善盟"};
    private String[] cityXinJiang = new String[]{"乌鲁木齐市", "克拉玛依市", "吐鲁番地区", "哈密地区", "昌吉回族自治州", "博尔塔拉蒙古自治州", "巴音郭楞蒙古自治州", "阿克苏地区", "克孜勒苏柯尔克孜自治州",
            "喀什地区", "和田地区", "伊犁哈萨克自治州", "塔城地区", "阿勒泰地区", "石河子市", "阿拉尔市", "图木舒克市", "五家渠市"};
    //    private String[] cityHK = new String[]{"中西区", "东区", "九龙城区", "观塘区", "南区", "深水埗区", "黄大仙区", "湾仔区", "油尖旺区", "离岛区", "葵青区", "北区", "西贡区", "沙田区", "屯门区", "大埔区", "荃湾区", "元朗区"};
    private String[] cityHK = new String[]{};
    //    private String[] cityMacao = new String[]{"花地玛堂区", "圣安多尼堂区", "大堂区", "望德堂区", "风顺堂区", "路环"};
    private String[] cityMacao = new String[]{};


    HashMap<Integer, String[]> map = new HashMap<Integer, String[]>();


    private WheelView province, city;

    /**
     * 设置是否循环滚动
     */
    private boolean cyclic = true;

    private List<String> provincesListSource, cityListSource;

    public CityPickerView(Context context) {
        this(context, null);
    }

    public CityPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CityPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        map.put(0, cityBeijing);
        map.put(1, cityTianjin);
        map.put(2, cityShangHai);
        map.put(3, cityChongQing);
        map.put(4, cityHebei);
        map.put(5, cityShanXi);
        map.put(6, cityJiangSu);
        map.put(7, cityZheJiang);
        map.put(8, cityAnHui);
        map.put(9, cityFuJian);
        map.put(10, cityJiangXi);
        map.put(11, cityShanDong);
        map.put(12, cityHeNan);
        map.put(13, cityHuBei);
        map.put(14, cityHuNan);
        map.put(15, cityGuangDong);
        map.put(16, cityLiaoNing);
        map.put(17, cityJiLin);
        map.put(18, cityHeilongjiang);
        map.put(19, cityGuangXi);
        map.put(20, cityHaiNan);
        map.put(21, cityTaiWan);
        map.put(22, citySichuan);
        map.put(23, cityGuizhou);
        map.put(24, cityYunNan);
        map.put(25, cityXiZang);
        map.put(26, cityShanXi);
        map.put(27, cityGanSu);
        map.put(28, cityQinghai);
        map.put(29, cityNingxia);
        map.put(30, cityInnerMongolia);
        map.put(31, cityXinJiang);
        map.put(32, cityHK);
        map.put(33, cityMacao);


        initBase(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CityPickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initBase(context, attrs, defStyleAttr);
    }

    private void initBase(Context context, AttributeSet attrs, int defStyleAttr) {
        initView();
        setListener();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initData();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_picker_city, this, true);
        province = (WheelView) findViewById(R.id.province);
        city = (WheelView) findViewById(R.id.city);
    }

    private void initData() {

    }

    public void setCyclic(boolean cyclic) {
        this.cyclic = cyclic;
        province.setCyclic(cyclic);
        city.setCyclic(cyclic);
    }

    public void setData() {
        provincesListSource = new ArrayList<>();
        provincesListSource.addAll(Arrays.asList(provinces));
        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter<String>((ArrayList<String>) provincesListSource);
        province.setAdapter(arrayWheelAdapter);
        province.setCurrentItem(0);
        cityListSource = new ArrayList<>();
        province.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                cityListSource.clear();
                cityListSource.addAll(Arrays.asList(map.get(index)));
                ArrayWheelAdapter cityArrayWheelAdapter = new ArrayWheelAdapter<String>((ArrayList<String>) cityListSource);
                city.setAdapter(cityArrayWheelAdapter);
                city.setCurrentItem(0);
            }
        });

        cityListSource.clear();
        cityListSource.addAll(Arrays.asList(cityBeijing));
        ArrayWheelAdapter cityArrayWheelAdapter = new ArrayWheelAdapter<String>((ArrayList<String>) cityListSource);
        city.setAdapter(cityArrayWheelAdapter);
        city.setCurrentItem(0);


    }

    public void setData(ArrayList<String> items) {
        provincesListSource = items;
        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter<String>(items);
        province.setAdapter(arrayWheelAdapter);
    }

    private void setListener() {
    }

    public int getCurrentItem() {
        return province.getCurrentItem();
    }

    public String getProvinceItem() {
        if (null == provincesListSource || provincesListSource.isEmpty()) {
            return null;
        }
        int position = province.getCurrentItem();
        if (position < 0 || position >= provincesListSource.size()) {
            return null;
        }

        return provincesListSource.get(position);
    }

    public String getCityItem() {
        if (null == cityListSource || cityListSource.isEmpty()) {
            return null;
        }
        int position = city.getCurrentItem();
        if (position < 0 || position >= cityListSource.size()) {
            return null;
        }

        return cityListSource.get(position);
    }
}