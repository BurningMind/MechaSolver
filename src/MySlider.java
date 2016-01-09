import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.event.*;

public class MySlider extends JSlider {

public int m_id=0;

    //Constructor
    public MySlider (int orient, int start, int end, int value, int id) {
        super (orient, start, end, value);
        m_id = id;
    }

}
