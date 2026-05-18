package br.com.eduardopirolo.fruitninja3;

import com.fazecast.jSerialComm.SerialPort;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MpuSerialReader {
    private SerialPort port;
    private BufferedReader reader;
    private String connectedPortName = "";

    public volatile float ax = 0f;
    public volatile float ay = 0f;
    public volatile float az = 0f;
    public volatile float gx = 0f;
    public volatile float gy = 0f;
    public volatile float gz = 0f;

    private volatile boolean connected = false;

    public boolean conectar(String porta) {
        try {
            port = SerialPort.getCommPort(porta);
            port.setBaudRate(115200);
            port.setNumDataBits(8);
            port.setNumStopBits(SerialPort.ONE_STOP_BIT);
            port.setParity(SerialPort.NO_PARITY);
            port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);

            if (!port.openPort()) {
                System.out.println("Nao foi possivel abrir porta: " + porta);
                return false;
            }

            connected = true;
            connectedPortName = porta;
            reader = new BufferedReader(new InputStreamReader(port.getInputStream()));

            Thread leituraThread = new Thread(this::lerDados, "mpu-serial-reader");
            leituraThread.setDaemon(true);
            leituraThread.start();

            System.out.println("Porta serial aberta: " + porta);
            return true;
        } catch (Exception e) {
            System.out.println("Erro serial: " + e.getMessage());
            return false;
        }
    }

    private void lerDados() {
        while (connected) {
            try {
                String linha = reader.readLine();
                if (linha == null) {
                    continue;
                }

                String[] partes = linha.trim().split(",");
                if (partes.length != 6) {
                    continue;
                }

                ax = Float.parseFloat(partes[0]);
                ay = Float.parseFloat(partes[1]);
                az = Float.parseFloat(partes[2]);
                gx = Float.parseFloat(partes[3]);
                gy = Float.parseFloat(partes[4]);
                gz = Float.parseFloat(partes[5]);
            } catch (Exception ignored) {
                // Ignora linhas incompletas ou mensagens de inicializacao do ESP32.
            }
        }
    }

    public boolean isConnected() {
        return connected && port != null && port.isOpen();
    }

    public String getConnectedPortName() {
        return connectedPortName;
    }

    public static String[] listarPortasDisponiveis() {
        SerialPort[] ports = SerialPort.getCommPorts();
        String[] nomes = new String[ports.length];
        for (int i = 0; i < ports.length; i++) {
            nomes[i] = ports[i].getSystemPortName();
        }
        return nomes;
    }

    public void desconectar() {
        connected = false;

        try {
            if (reader != null) {
                reader.close();
            }
        } catch (Exception ignored) {
        }

        try {
            if (port != null && port.isOpen()) {
                port.closePort();
            }
        } catch (Exception ignored) {
        }

        connectedPortName = "";
        System.out.println("Porta serial fechada");
    }
}
