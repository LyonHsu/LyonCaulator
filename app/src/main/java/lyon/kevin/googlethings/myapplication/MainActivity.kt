package lyon.kevin.googlethings.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    val  TAG:String? = MainActivity::class.simpleName.toString()
    var array:Array<String> = emptyArray<String>()
    val spanCount = 4;
    lateinit var textView: TextView
    var isStartCal = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        array = arrayOf("1","2","3","+",
                        "4","5","6","-",
                        "7","8","9","*",
                        "C","0","=","/"
                       )
        for(i in array.indices) {
            Log.d(TAG,"array["+i+"]:"+array[i]);
        }
        initView();
    }

    private fun initView(){
        textView = findViewById<TextView>(R.id.title)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation =RecyclerView.VERTICAL// RecyclerView.HORIZONTAL
        val gridLayoutManager = GridLayoutManager(this,spanCount)
        val testAdapter = TestAdaper(this, array)
        testAdapter.setOnItemClick(object :TestAdaper.ItemOnCLick{
            override fun OnCLick(holder: TestAdaper.ViewHolder, position: Int) {

                if(!isStartCal){
                    isStartCal = true;
                    textView.text=""
                }
                var v = array[position]
                Log.d(TAG,"you chose:"+v)
                when(v){
                    "1"   -> textView.append(v)
                    "2"   -> textView.append(v)
                    "3"   -> textView.append(v)
                    "4"   -> textView.append(v)
                    "5"   -> textView.append(v)
                    "6"   -> textView.append(v)
                    "7"   -> textView.append(v)
                    "8"   -> textView.append(v)
                    "9"   -> textView.append(v)
                    "0"   -> textView.append(v)
                    "+"   -> textView.append(v)
                    "-"   -> textView.append(v)
                    "*"   -> textView.append(v)
                    "/"   -> textView.append(v)
                    "C"   -> setClear()
                    "c"   -> setClear()
                    "="   ->  textView.text = theAns(textView.text.toString()).toString()
                }
            }

        })
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = testAdapter
    }

    fun theAns(cal:String):Double{
        Log.d(TAG,"you complent :"+cal)
        var ans =0.0;
        if(cal.contains("+")) {
            ans = theAdd(cal);
        }else if(cal.contains("-")){
            ans = theSubtraction(cal);
        }else if(cal.contains("*")){
            ans = theMultiplication(cal);
        }else if(cal.contains("/")){
            ans = theDivision(cal);
        }else{
            if(cal.isEmpty()){
                ans=0.0
            }else
                ans=cal.toDouble()
        }

        Log.d(TAG,"you complent ans:"+ans)

        return ans;
    }

    fun theAdd(cal:String):Double{
        var ans =0.0;
        Log.d(TAG,"you theAdd complent :"+cal)
        var i = cal.split("+")
        if(i.size>1) {
            for (j in i) {
                ans += theAns(j)
            }
        }else{
            ans=i.get(0).toDouble();
        }
        Log.d(TAG,"you theAdd ans:"+ans)
        return ans;
    }

    fun theSubtraction(cal:String):Double{
        var first = true
        var ans :Double = 0.0;
        Log.d(TAG,"you theSubtraction complent :"+cal)
        var i = cal.split("-")
        if(i.size>1) {
            for (j in i) {
                if(first && theAns(j)>=0.0){
                    first = false
                    ans = theAns(j)
                }else {
                    ans -= theAns(j)
                }
            }
        }else{
            ans=i.get(0).toDouble();
        }
        Log.d(TAG,"you theSubtraction ans:"+ans)
        return ans;
    }

    fun theMultiplication(cal:String):Double{
        var ans =0.0;
        Log.d(TAG,"you theMultiplication complent :"+cal)
        var i = cal.split("*")
        if(i.size>1) {
            for (j in i) {
                if(ans==0.0){
                    ans = j.toDouble()
                }else {
                    ans *= theAns(j)
                }
            }
        }else{
            ans=i.get(0).toDouble();
        }
        Log.d(TAG,"you theMultiplication ans:"+ans)
        return ans
    }

    fun theDivision(cal:String):Double{
        var ans =0.0;
        Log.d(TAG,"you theDivision complent :"+cal)
        var i = cal.split("/")
        if(i.size>1) {
            for (j in i) {
                if(ans==0.0){
                    ans = j.toDouble()
                }else {
                    ans /= theAns(j)
                }
            }
        }else{
            ans=i.get(0).toDouble();
        }
        Log.d(TAG,"you theDivision ans:"+ans)
        return ans
    }

    fun setClear(){
        textView.setText("0")
        isStartCal = false;
    }
}
