package lyon.kevin.googlethings.myapplication

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView


class TestAdaper : RecyclerView.Adapter<TestAdaper.ViewHolder>{
    lateinit var holder: ViewHolder
    private var context:Context
    lateinit var array:Array<String>
    interface ItemOnCLick {
        fun OnCLick(holder: ViewHolder,position: Int)
    }
    lateinit var itemOnCLick:ItemOnCLick


    constructor(context: Context, array: Array<String>) : super() {
        this.context = context
        this.array = array
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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
//            AlertDialog.Builder(context).setMessage("you chose:"+model).setCancelable(true).create().show()
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