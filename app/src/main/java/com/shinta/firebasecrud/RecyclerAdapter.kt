package com.shinta.firebasecrud

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(private var listMahasiswa: ArrayList<data_mhs>, context: Context) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var context: Context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val NIM: TextView
        val Nama: TextView
        val Jurusan: TextView
        val ListItem: LinearLayout

        init {

            NIM = itemView.findViewById(R.id.nimx)
            Nama = itemView.findViewById(R.id.namax)
            Jurusan = itemView.findViewById(R.id.jurusanx)
            ListItem = itemView.findViewById(R.id.list_item)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val V: View =
            LayoutInflater.from(parent.getContext()).inflate(R.layout.view_design, parent, false)
        return ViewHolder(V)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        val NIM: String? = listMahasiswa.get(position).nim
        val Nama: String? = listMahasiswa.get(position).nama
        val Jurusan: String? = listMahasiswa.get(position).jurusan

        holder.NIM.text = "NIM: $NIM"
        holder.Nama.text = "Nama: $Nama"
        holder.Jurusan.text = "Jurusan: $Jurusan"
        holder.ListItem.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                holder.ListItem.setOnLongClickListener { view ->
                    val action = arrayOf("Update", "Delete")
                    val alert: AlertDialog.Builder = AlertDialog.Builder(view.context)
                    alert.setItems(action, DialogInterface.OnClickListener { dialog, i ->
                        when (i) {

                            0 -> {
                                val bundle = Bundle()
                                bundle.putString("dataNIM", listMahasiswa[position].nim)
                                bundle.putString("dataNama", listMahasiswa[position].nama)
                                bundle.putString("dataJurusan", listMahasiswa[position].jurusan)
                                bundle.putString("getPrimaryKey", listMahasiswa[position].key)
                                val intent = Intent(view.context, Update::class.java)
                                intent.putExtras(bundle)
                                context.startActivity(intent)
                            }
                            1 -> {
                                listener?.onDeleteData(listMahasiswa.get(position), position)
                            }
                        }
                    })
                    alert.create()
                    alert.show()
                    true
                }
                return true
            }
        })
    }

    override fun getItemCount(): Int {
        return listMahasiswa.size
    }

    var listener: dataListener? = null
    init {
        this.context = context
        this.listener = context as ListData
    }

    interface dataListener {
        fun onDeleteData(data: data_mhs?, position: Int)
    }

}