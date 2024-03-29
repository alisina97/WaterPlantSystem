import Yu.CountdownHandlerPart3;
import com.fazecast.jSerialComm.SerialPort;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Timer;

public class main {
    public static final byte TIMER_DURATION = 10;

    public static void main(String[] args) {
        var sp = SerialPort.getCommPort("COM4");

        sp.setComPortParameters(9600, Byte.SIZE, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        sp.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);

        var hasOpened = sp.openPort();

        if (!hasOpened) {
            throw new IllegalArgumentException("Failed to open port.");
        }
            var outputStream = sp.getOutputStream();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    outputStream.close();
                }
                catch ( IOException e){
                    e.printStackTrace();
                }
            }));

            var timer = new Timer();
            var countdown = new CountdownHandlerPart3(TIMER_DURATION, outputStream);

            sp.addDataListener(countdown);

            System.out.println("Listen: " + countdown.getListeningEvents());
            timer.schedule(countdown, 0, 1000);
        }
    }

