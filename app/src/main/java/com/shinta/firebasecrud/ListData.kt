package com.shinta.firebasecrud

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ListData : AppCompatActivity(), RecyclerAdapter.dataListener {

    private var recyclerView: RecyclerView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    val database = FirebaseDatabase.getInstance()
    private var dataMahasiswa = ArrayList<data_mhs>()
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_data)
        recyclerView = findViewById(R.id.datalist)
        supportActionBar!!.title = "Data Mahasiswa"
        auth = FirebaseAuth.getInstance()
        MyRecyclerView()
        GetData()
    }

    private fun GetData() {
        Toast.makeText(applicationContext, "Mohon Tunggu Sebentar...", Toast.LENGTH_SHORT).show()
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val getReference = database.getReference()
        getReference.child("Admin").child(getUserID).child("Mahasiswa")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        dataMahasiswa.clear()
                        for (snapshot in dataSnapshot.children) {
                            val mahasiswa = snapshot.getValue(data_mhs::class.java)
                            mahasiswa?.key = snapshot.key
                            dataMahasiswa.add(mahasiswa!!)
                        }
                        adapter = RecyclerAdapter(dataMahasiswa, this@ListData)
                        recyclerView?.adapter = adapter
                        recyclerView?.adapter?.notifyDataSetChanged()
                        Toast.makeText(applicationContext,"Data Berhasil Dimuat", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                    Toast.makeText(applicationContext, "Data Gagal Dimuat", Toast.LENGTH_SHORT).show()
                    Log.e("MyListActivity", databaseError.details + " " + databaseError.message)
                }
            })
    }

    private fun MyRecyclerView() {
        layoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)

        val itemDecoration = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.line)!!)
        recyclerView?.addItemDecoration(itemDecoration)
    }

    override fun onDeleteData(data: data_mhs?, position: Int) {
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val getReference = database.getReference()
        val getKey = data?.key.toString()
        if(getReference != null){
            getReference.child("Admin")
                .child(getUserID)
                .child("Mahasiswa")
                .child(getKey!!)
                .removeValue()
                .addOnSuccessListener {
                    Toast.makeText(this@ListData, "Data Berhasil Dihapus",
                        Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this@ListData, "Reference kosong",
                Toast.LENGTH_SHORT).show()
        }
    }
}