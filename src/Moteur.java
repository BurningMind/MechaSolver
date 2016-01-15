import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Moteur implements ActionListener{

    public Joint m_joint;
    public double m_speed;
    public Timer monTimer;
    public MainWindow m_mainWindow;

    //Constructor
    public Moteur (Joint joint, double speed, MainWindow mainWindow) {
        m_joint = joint;
        m_speed = speed;
        m_mainWindow = mainWindow;
        monTimer = new Timer(34, this);
        monTimer.start();
    }

    public void actionPerformed(ActionEvent e) {
        double angleInRad = 0.0;
        if (m_joint.m_anchor.m_isGround) {
            angleInRad = ((m_joint.m_freeSolid.m_angle - m_joint.m_anchor.m_angle)+Math.toRadians(1)) % (Math.PI*2);
        } else {
            angleInRad = ((m_joint.m_freeSolid.m_angle - (m_joint.m_anchor.m_angle - Math.PI)+Math.PI*2)+Math.toRadians(1))% (Math.PI*2);
        }

        m_mainWindow.settingValue = true;
        m_mainWindow.m_sliders.get(m_joint.m_id).setValue((int)Math.toDegrees(angleInRad));
        m_mainWindow.setJointAngle(m_joint,angleInRad);
        m_mainWindow.settingValue = false;

        m_mainWindow.repaint();
    }

}
