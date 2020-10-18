package ofy.livegol.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ofy.livegol.Giris
import ofy.livegol.R
import java.util.*


class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(p0: RemoteMessage) {
        val baslik = p0.data.get("baslik")
        val mesaj = p0.data.get("mesaj")
        val bildirimkatgori = p0.data.get("katagori")
        bildirimdurumcek(baslik, mesaj,bildirimkatgori)
    }

    private fun bildirimdurumcek(baslik: String?, mesaj: String?,katagori:String?) {
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("ayarlar/bildirim")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        var durum = true
                        if (p0.exists()) {
                            durum = p0.getValue() as Boolean
                        }
                        bildirimgonder(baslik, mesaj,katagori, durum)
                    }

                })

    }


    private fun bildirimgonder(baslik: String?, mesaj: String?,katagori:String?, durum: Boolean) {
        if (durum) {
            val rand=Random()
            val bildirimid =rand.nextInt(1000)+1
            val kanal_id = "LivegolKanal"+bildirimid.toString()
            val name = getString(R.string.Kanaladi)
            val notificintt = Intent(this, Giris::class.java)
            val contentintetnt = PendingIntent.getActivity(this, 0, notificintt, 0)
            val builder = NotificationCompat.Builder(this, kanal_id)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentTitle(baslik)
                    .setContentText(mesaj)
                    .setColor(resources.getColor(R.color.colorPrimaryDark2))
                    .setSmallIcon(R.drawable.livetahmin_bildirim)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(mesaj))
                    .setOnlyAlertOnce(false)
                    .setAutoCancel(true)
            if (katagori!=null){
                builder.setSubText(katagori)
            }
            val noti = builder.build()
            noti.contentIntent = contentintetnt
            val notimanager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val onemlilik = NotificationManager.IMPORTANCE_HIGH
                val benimkanalim = NotificationChannel(kanal_id, name, onemlilik)
                notimanager.createNotificationChannel(benimkanalim)
            }
            notimanager.notify(bildirimid, noti)
        }
    }
    private fun TokeniKaydet(token: String) {
        if (FirebaseAuth.getInstance().currentUser != null) {
            FirebaseDatabase.getInstance().getReference()
                    .child("Kullanicilar")
                    .child(FirebaseAuth.getInstance().uid!!)
                    .child("token")
                    .setValue(token)
        }
    }

}