package com.example.notificaciones;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_RESPUESTA_POR_VOZ = "extra_respuesta_por_voz";
    public static final String EXTRA_MESSAGE = "com.example.notificaciones.EXTRA_MESSAGE";
    public static final String ACTION_DEMAND = "com.example.notificaciones.ACTION_DEMAND";
    final static String MI_GRUPO_DE_NOTIFIC = "mi_grupo_de_notific";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Miramos si hemos recibido una respuesta por voz
        Bundle respuesta = RemoteInput.getResultsFromIntent(getIntent());
        if (respuesta != null) {
            CharSequence texto = respuesta.getCharSequence(EXTRA_RESPUESTA_POR_VOZ);
            ((TextView) findViewById(R.id.textViewRespuesta)).setText(texto);
        }


        Button wearButton = (Button) findViewById(R.id.boton1);
        wearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intencionLlamar = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:555123456"));
                PendingIntent intencionPendienteLlamar = PendingIntent.getActivity(MainActivity.this, 0, intencionLlamar, 0);

                // Creamos intención pendiente
                //Intent intencionMapa = new Intent(MainActivity.this, MainActivity.class);
                Intent intencionMapa = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=universidad+politecnica+valencia"));
                PendingIntent intencionPendienteMapa = PendingIntent.getActivity(MainActivity.this, 0, intencionMapa, 0);
                int notificacionId = 001;

                NotificationCompat.Action accion = new NotificationCompat.Action.Builder(R.mipmap.ic_action_call, "llamar Wear", intencionPendienteLlamar).build();
                List<NotificationCompat.Action> acciones = new ArrayList<NotificationCompat.Action>();
                acciones.add(accion);
                acciones.add(new NotificationCompat.Action(R.mipmap.ic_action_locate, "Ver mapa", intencionPendienteMapa));


                String s = "Texto largo con descripción detallada de la notificación. ";


                // Creamos un BigTextStyle para la segunda página
                NotificationCompat.BigTextStyle segundaPg = new NotificationCompat.BigTextStyle();
                segundaPg.setBigContentTitle("Página 2").bigText("Más texto.");

                // Creamos una notification para la segunda página
                Notification notificacionPg2 = new NotificationCompat.Builder(MainActivity.this).setStyle(segundaPg).build();

                NotificationCompat.BigTextStyle terceraPg = new NotificationCompat.BigTextStyle();
                terceraPg.setBigContentTitle("Página 3").bigText("Texto 3era página.");

                // Creamos una notification para la segunda página
                Notification notificacionPg3 = new NotificationCompat.Builder(MainActivity.this).setStyle(terceraPg).build();


                NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender().setHintHideIcon(true).setBackground(BitmapFactory.decodeResource(getResources(), R.drawable.escudo_upv))
                        .addActions(acciones).addPage(notificacionPg2).addPage(notificacionPg3);

                Notification notificacion = new NotificationCompat.Builder(MainActivity.this)
                        .setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Título").setContentText(Html.fromHtml("<b>Notificación</b> <u>Android <i>Wear</i></u>")).setContentIntent(intencionPendienteMapa)
                        .addAction(R.mipmap.ic_action_call, "llamar", intencionPendienteLlamar)
                        //.extend(new NotificationCompat.WearableExtender().addActions( acciones))
                        //.setLargeIcon(BitmapFactory.decodeResource( getResources(), R.drawable.escudo_upv))
                        .extend(wearableExtender)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(s + s + s + s))
                        .setGroup(MI_GRUPO_DE_NOTIFIC)
                        .build();
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                notificationManager.notify(notificacionId, notificacion);

                int idNotificacion2 = 002;
                Notification notificacion2 = new NotificationCompat.Builder(MainActivity.this).setContentTitle("Nueva Conferencia").setContentText("Los neutrinos").setSmallIcon(R.mipmap.ic_action_mail_add).setGroup(MI_GRUPO_DE_NOTIFIC).build();
                notificationManager.notify(idNotificacion2, notificacion2);

                // Creamos una notificacion resumen
                int idNotificacion3 = 003;
                Notification notificacion3 = new NotificationCompat.Builder(MainActivity.this).setContentTitle("2 notificaciones UPV").setSmallIcon(R.mipmap.ic_action_attach)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.escudo_upv)).setStyle(new NotificationCompat.InboxStyle().addLine("Nueva Conferencia Los neutrinos").addLine("Nuevo curso Android Wear").setBigContentTitle("2 notificaciones UPV").setSummaryText("info@upv.es")).setNumber(2)
                        .setGroup(MI_GRUPO_DE_NOTIFIC).setGroupSummary(true).build();
                notificationManager.notify(idNotificacion3, notificacion3);

            }
        });

        Button butonVoz = (Button) findViewById(R.id.boton_voz);
        butonVoz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos una intención de respuesta
                Intent intencion = new Intent(MainActivity.this, MainActivity.class);
                PendingIntent intencionPendiente = PendingIntent.getActivity(MainActivity.this, 0, intencion, PendingIntent.FLAG_UPDATE_CURRENT);

                // Creamos la entrada remota para añadirla a la acción
                String[] opcRespuesta = getResources().getStringArray(R.array.opciones_respuesta);
                RemoteInput entradaRemota = new RemoteInput.Builder(EXTRA_RESPUESTA_POR_VOZ).setLabel("respuesta por voz").setChoices(opcRespuesta).build();

                // Creamos la acción
                NotificationCompat.Action accion = new NotificationCompat.Action.Builder(android.R.drawable.ic_menu_set_as, "responder", intencionPendiente).addRemoteInput(entradaRemota).build();

                // Creamos la notificación
                int idNotificacion = 002;
                NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(MainActivity.this).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Respuesta por Voz").setContentText("Indica una respuesta").extend(new NotificationCompat.WearableExtender().addAction(accion));

                // Lanzamos la notificación
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                notificationManager.notify(idNotificacion, notificationBuilder.build());
            }
        });


        Button butonVozAnuncio = (Button) findViewById(R.id.boton_voz_anuncio);
        butonVozAnuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos una intención de respuesta

                Intent intencion = new Intent(MainActivity.this, WearReceiver.class).putExtra(EXTRA_MESSAGE, "alguna información relevante").setAction(ACTION_DEMAND);
                PendingIntent intencionPendiente = PendingIntent.getBroadcast(MainActivity.this, 0, intencion, 0);

                // Creamos la entrada remota para añadirla a la acción
                String[] opcRespuesta = getResources().getStringArray(R.array.opciones_respuesta);
                RemoteInput entradaRemota = new RemoteInput.Builder(EXTRA_RESPUESTA_POR_VOZ).setLabel("respuesta por voz").setChoices(opcRespuesta).build();

                // Creamos la acción
                NotificationCompat.Action accion = new NotificationCompat.Action.Builder(android.R.drawable.ic_menu_set_as, "responder", intencionPendiente).addRemoteInput(entradaRemota).build();

                // Creamos la notificación
                int idNotificacion = 002;
                NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(MainActivity.this).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Respuesta por Voz").setContentText("Indica una respuesta").extend(new NotificationCompat.WearableExtender().addAction(accion));

                // Lanzamos la notificación
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                notificationManager.notify(idNotificacion, notificationBuilder.build());
            }
        });
    }
}

