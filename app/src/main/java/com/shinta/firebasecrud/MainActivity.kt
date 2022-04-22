package com.shinta.firebasecrud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var auth: FirebaseAuth? = null
    private val RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logout.setOnClickListener(this)
        save.setOnClickListener(this)
        showdata.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()
    }

    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.getId()) {

                R.id.save -> {
                    val getUserID = auth!!.currentUser!!.uid
                    val database = FirebaseDatabase.getInstance()
                    val getNIM: String = nim.getText().toString()
                    val getNama: String = nama.getText().toString()
                    val getJurusan: String = jurusan.getText().toString()
                    val getReference: DatabaseReference
                    getReference = database.reference

                    if (isEmpty(getNIM) || isEmpty(getNama) || isEmpty(getJurusan)) {
                        Toast.makeText(this@MainActivity, "Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
                    } else {
                        getReference.child("Admin").child(getUserID).child("Mahasiswa").push()
                            .setValue(data_mhs(getNIM, getNama, getJurusan))
                            .addOnCompleteListener(this) {
                                nim.setText("")
                                nama.setText("")
                                jurusan.setText("")
                                Toast.makeText(this@MainActivity, "Data Tersimpan", Toast.LENGTH_SHORT).show()
                            }
                    }
                }

                R.id.showdata -> {
                    startActivity(Intent(this@MainActivity, ListData::class.java))
                }

                R.id.logout ->
                    AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(object : OnCompleteListener<Void> {
                            override fun onComplete(p0: Task<Void>) {
                                Toast.makeText(this@MainActivity, "Logout Berhasil",
                                    Toast.LENGTH_SHORT).show()
                                intent = Intent(applicationContext, Login::class.java)
                                startActivity(intent)
                                finish()
                            }
                        })
            }
        }
    }
}