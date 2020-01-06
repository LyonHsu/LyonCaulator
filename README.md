# Lyon Caulator

使用到RecyclerView 套件 Ref:https://tpu.thinkpower.com.tw/tpu/articleDetails/1274
使用到interface 使得adapter 可以直接使用onItemOnClick

Ref:
基本kotiln:
https://www.kotlincn.net/docs/reference/control-flow.html#for-%E5%BE%AA%E7%8E%AF

RecyclerView 套件:
https://tpu.thinkpower.com.tw/tpu/articleDetails/1274

四則運算邏輯：
https://medium.com/%E7%A8%8B%E5%BC%8F%E8%A8%AD%E8%A8%88%E4%B8%8D%E6%98%AF%E9%AD%94%E6%B3%95/javascript-%E8%A8%88%E7%AE%97%E6%A9%9F-38e7314a6a4e

1.首先我們使用RecyclerView 來製作九宮格數字按鈕
layout/activity_main.xml

  <?xml version="1.0" encoding="utf-8"?>
  <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto"
      xmlns:tools="http://schemas.android.com/tools"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      tools:context=".MainActivity">
      <TextView
              android:id="@+id/title"
              android:layout_width="wrap_content"
              android:layout_height="wrap_content"
              android:text="@string/app_name"
              android:layout_margin="10dp"
              app:layout_constraintBottom_toBottomOf="parent"
              app:layout_constraintLeft_toLeftOf="parent"
              app:layout_constraintRight_toRightOf="parent"
              app:layout_constraintTop_toTopOf="parent" />
      <androidx.recyclerview.widget.RecyclerView
              android:id="@+id/recyclerView"
              android:layout_width="395dp"
              android:layout_height="340dp"
              android:layout_marginTop="10dp"
              android:layout_below="@+id/title"
              android:padding="10dp"
      />
  </RelativeLayout>
  2.接著我們來製作 TestAdapter
  
    class TestAdaper : RecyclerView.Adapter<TestAdaper.ViewHolder>{
        lateinit var holder: ViewHolder
        private var context:Context
        lateinit var array:Array<String>
        //為了方便在MainActivity呼叫子物件的點擊動作，這裏使用了interface
        interface ItemOnCLick {
            fun OnCLick(holder: ViewHolder,position: Int)
        }
        lateinit var itemOnCLick:ItemOnCLick
        constructor(context: Context, array: Array<String>) : super() {
            this.context = context
            this.array = array
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            //加入RecyclerView item要使用的layout
            val cell = LayoutInflater.from(context).inflate(R.layout.caulator_cell, parent, false)
            val viewHolder = ViewHolder(cell)
            this.holder=viewHolder;
            viewHolder.videoBtn = cell.findViewById(R.id.button)
            return holder
        }
        override fun getItemCount(): Int {
            return array.size
        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val model = array[position]
            holder.videoBtn.text=model;
            holder.videoBtn.setOnClickListener {
                if(itemOnCLick!=null)
                    itemOnCLick.OnCLick(holder,position);
            }
        }
        public fun setOnItemClick(itemOnCLick:ItemOnCLick ){
            this.itemOnCLick=itemOnCLick;
        }
        class ViewHolder : RecyclerView.ViewHolder {
            lateinit var videoBtn:Button
            constructor(itemView: View) : super(itemView)
        }
    }
  
  
  3.然後在MainActivity.kt 
  
      val spanCount = 4;
      var array = arrayOf("1","2","3","+",
                        "4","5","6","-",
                        "7","8","9","*",
                        "C","0","=","/"
                       )
      private fun initView(){
        textView = findViewById<TextView>(R.id.title)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val linearLayoutManager = LinearLayoutManager(this)
        //這裡加入RecyclerView顯示為直式或是橫式
        linearLayoutManager.orientation =RecyclerView.VERTICAL// RecyclerView.HORIZONTAL
        //設定每列顯示數量
        val gridLayoutManager = GridLayoutManager(this,spanCount)
        val testAdapter = TestAdaper(this, array)
        //由於我們有製作interface來方便呼叫ItemClick，所以這裡可以直接使用
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
    
    4.四則運算邏輯
    由於數學有先加減後乘除規則，所以這裡使用textView來記錄要計算的運算，而後經過遞迴來處理先加減後乘除的規則。注意這只是範例，所以沒有考慮硬體消耗問  題。 有興趣的可以玩一下！
    使用切割法，先將＋,-切割開來，如此將會剩下*,/ ，然後我們來處理*,/的運算再將結果回傳，如此將會只剩下＋,-的運算需要計算。
    Ex:
    4+5*3-6/3=  4    +  (5*3)  -  (6/3) =4+  15  -   2  =  17 
    
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
    


