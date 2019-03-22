package lens;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Nil
 */
public class Ventana extends JFrame {

    
    private JPanel botPanel;
    private JPanel topPanel;
    
    private JTextField textoFoco;
    private JTextField textoDisLente;
    private JTextField textoObjeto;
    private JTextField alturaObjeto;
    
    private JLabel Objeto;
    private JLabel lented;
    private JLabel lentec;
    private JLabel imagen;
    private JLabel[] arrayLentes = new JLabel[23];
    
    private int nLente = 0;  // indica cuantos lentes hay. (n-1 seria la posicion)
    private int lentei = 0;
    
    private JButton addObject;
    private JButton addLente;
    
    private JPopupMenu popup;
    private JPopupMenu popup2;
    
    private float lentePosIni = 450;
    private float posAcumuladoX = 450;
    private float lentePosDx  = 0;
    private float objetoPosX;
    
    private float Xtemp = 0;
    private float Ytemp = 0;
    
    public static final Color PURPLE = new Color(128,0,128);
  
    public Ventana(){
        // Foco amarillo debido a ... Tools -> Options -> Editor -> Initializacion
        setTitle("Lentes Fisica IV - UNI FC");
        // setBounds(0,0,1200,600);   (0,0) setLocation y (1200,600) setSize
        setSize(1200,600);
        setLocationRelativeTo(null);  // Centrar
        setMinimumSize(new Dimension(1200,600)); //Tamaño minimo de ventana
        
        iniciarComponentes();

        setDefaultCloseOperation(3); // 3 -> Terminar el programa al cerrar.
    }
 
    

  
    private void iniciarComponentes(){
    
        JPanel mainPanel = new JPanel(new BorderLayout()); //BorderLayout para redimensionar
        //mainPanel.setLayout(null);
        
        topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(1200,500));
        topPanel.setLayout(null);
       
        
        botPanel = new JPanel();
        botPanel.setPreferredSize(new Dimension(1200,100));
        botPanel.setLayout(null);
         
        iniciarEtiquetas();     
        iniciarTextFields();
        iniciarButtons();

        Objeto = new JLabel(new ImageIcon("objeto.png"));
        //topPanel.add(Objeto);
        
        imagen = new JLabel(new ImageIcon("imagen.jpg"));
        imagen.setBounds(550,140,25,142);
    
        
        //AQUII
        lented = new JLabel(new ImageIcon("lented.jpg"));
      //  lented.setBounds((int)(lentePosIni+lentePosDx),180,25,142);
      //  System.out.println("va: " + lentePosIni+lentePosDx);
        
        
        lentec = new JLabel(new ImageIcon("lentec.jpg"));
      //  lentec.setBounds((int)(lentePosIni+lentePosDx),180,25,142);   
       
           
        //JPopMenu
        popup = new JPopupMenu();
        ActionListener menuListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                topPanel.remove(Objeto);
                topPanel.remove(imagen);
                addObject.setEnabled(true); 
               
                topPanel.revalidate();  
                repaint();
            
            }
        };
        
        JMenuItem item;
        popup.add(item = new JMenuItem("Eliminar"));
        item.addActionListener(menuListener);
        
        MouseListener oyenteMouse = new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                 checkPopup(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                 checkPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                 checkPopup(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                 checkPopup(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                 checkPopup(e);
            }
            
             private void checkPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                 popup.show(Objeto, topPanel.getX(), topPanel.getY());
                 }
                topPanel.revalidate();
                repaint(); // para no despintar linea negra de graphics
              }   
        };
        
        Objeto.addMouseListener(oyenteMouse);
     
        botPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
        //topPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED,Color.BLACK,Color.BLACK));      
        
   
        
        
        mainPanel.add(topPanel);
        mainPanel.add(botPanel, BorderLayout.SOUTH);

        this.getContentPane().add(mainPanel);
        
    }   
    
    private void iniciarEtiquetas(){
        
        JLabel etiquetaFoco = new JLabel();
        etiquetaFoco.setBounds(420, 20, 30, 20); // setLocation y setSize
        etiquetaFoco.setText("Foco");
        
        JLabel etiquetaObjeto = new JLabel();
        etiquetaObjeto.setBounds(420, 50, 50, 20); // setLocation y setSize
        etiquetaObjeto.setText("Objeto");
        
        botPanel.add(etiquetaFoco); //agregamos el label etiqueta al panel  
        botPanel.add(etiquetaObjeto);
    }
    
    private void iniciarTextFields(){
    
        textoFoco = new JTextField();
        textoFoco.setBounds(470,20,80,20);
        TextPrompt focoCm = new TextPrompt("En cm", textoFoco);
        focoCm.changeAlpha(0.75f);
        focoCm.changeStyle(Font.ITALIC);
        
        textoDisLente = new JTextField();
        textoDisLente.setBounds(555,20,80,20);
        TextPrompt disLenCm = new TextPrompt("Dis (cm)",textoDisLente);
        disLenCm.changeAlpha(0.75f);
        disLenCm.changeStyle(Font.ITALIC);
        textoDisLente.setEnabled(false);
        
        textoObjeto = new JTextField();
        textoObjeto.setBounds(470,50,80,20);
        TextPrompt distanciaCm = new TextPrompt("Pos.   (cm)", textoObjeto);
        distanciaCm.changeAlpha(0.75f);
        distanciaCm.changeStyle(Font.ITALIC);
        
        alturaObjeto = new JTextField();
        alturaObjeto.setBounds(555,50,80,20);
        TextPrompt alturaCm = new TextPrompt("Altura (cm)", alturaObjeto);
        alturaCm.changeAlpha(0.75f);
        alturaCm.changeStyle(Font.ITALIC);
        
        
        botPanel.add(textoObjeto);
        botPanel.add(alturaObjeto);
        botPanel.add(textoFoco);
        botPanel.add(textoDisLente);
        
    }
    
    private int Comparar(Object a){
        int stop = 0 ;
        for(int i = 0; i < 23; i ++){
            if(a == arrayLentes[i]){
                stop = i;
                break;            
            }
        }
        return stop;       
    }
    
    private void reducirLente(){        
        for( int i = lentei; i < nLente; i++) arrayLentes[i] = arrayLentes[i+1];
            nLente = nLente - 1;
    }
    
    private void iniciarButtons(){
        
        
        
        popup2 = new JPopupMenu();
        ActionListener menuListener2 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                topPanel.remove(arrayLentes[lentei]); // Solo se quita de pantalla
                reducirLente(); // quitarlo del array
                
                if(nLente == 0){
                    textoDisLente.setEnabled(false);
                    topPanel.remove(imagen);
                    lentePosDx = 0;
                    posAcumuladoX = lentePosIni;
                }else posAcumuladoX = arrayLentes[nLente-1].getX();
                /*
                siguiente linea error mio no funciona correctamente
                if(nLente == 0) {
                    textoDisLente.setEnabled(false);
                    lentePosDx = 0;
                    posAcumuladoX = lentePosIni;
                }*/

                topPanel.revalidate();  
                repaint();
            
            }
        };
        
        JMenuItem item;
        popup2.add(item = new JMenuItem("Eliminar"));
        item.addActionListener(menuListener2);
                 
        MouseListener oyenteMouseLente = new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                 checkPopup(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                 checkPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                 checkPopup(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                 checkPopup(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                 checkPopup(e);
            }
            
            private void checkPopup(MouseEvent e) {
                if (e.isPopupTrigger()){                
                    lentei = Comparar(e.getSource());
                    //siguiente linea un error mio. no funciona correctamente
                //    if(lentei == nLente-1) posAcumuladoX = posAcumuladoX - lentePosDx; //Solo si es el ultimo
                    popup2.show(arrayLentes[lentei], topPanel.getX(), topPanel.getY());
                }
                topPanel.revalidate();
                repaint(); // para no despintar linea negra de graphics
            }   
        };
        
      //  Objeto.addMouseListener(oyenteMouseLente);
        
        //Add Lente
        addLente = new JButton();
        addLente.setBounds(650,20,110,20);
        addLente.setText("Add Lente"); // SE AGREGA ATRAS DEL ULTIMO AGREGADO
        
        ActionListener oyenteLente = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
                if( (textoFoco.getText().matches("-?\\d+(\\.\\d+)?") && !textoDisLente.isEnabled()) 
                        || (textoFoco.getText().matches("-?\\d+(\\.\\d+)?") && textoDisLente.getText().matches("-?\\d+(\\.\\d+)?"))  ){ 
                    
                    
                    
                    if( Float.parseFloat(textoFoco.getText()) < 0 ){
                        arrayLentes[nLente] = new JLabel(new ImageIcon("lented.jpg"));
                        arrayLentes[nLente].addMouseListener(oyenteMouseLente);
                        
                       if( Objeto.getParent() == topPanel ){
                           topPanel.add(imagen);
                       }
                       if( textoDisLente.isEnabled() == true ){
                           lentePosDx = Integer.parseInt(textoDisLente.getText());                        
                           lentePosDx = lentePosDx*5;               
                       }

                       arrayLentes[nLente].setBounds((int)(posAcumuladoX+lentePosDx),180,25,142); //Nueva posicion
                       posAcumuladoX = posAcumuladoX + lentePosDx;
                       topPanel.add(arrayLentes[nLente]);
                       nLente = nLente + 1;          
                       topPanel.revalidate();
                       repaint();
                       
                    }else{
                        arrayLentes[nLente] = new JLabel(new ImageIcon("lentec.jpg"));
                        arrayLentes[nLente].addMouseListener(oyenteMouseLente);
                        
                       if( Objeto.getParent() == topPanel ){
                           topPanel.add(imagen);
                       }
                       if( textoDisLente.isEnabled() == true ){
                           lentePosDx = Integer.parseInt(textoDisLente.getText());                        
                           lentePosDx = lentePosDx*5;               
                       }

                       arrayLentes[nLente].setBounds((int)(posAcumuladoX+lentePosDx),180,25,142); //Nueva posicion
                       posAcumuladoX = posAcumuladoX + lentePosDx;
                       topPanel.add(arrayLentes[nLente]);
                       nLente = nLente + 1;
                       lentePosDx = lentePosDx+100;
                       topPanel.revalidate();
                       repaint(); 
                    }
                    textoFoco.setText("");
                    textoDisLente.setText("");
                    textoDisLente.setEnabled(true); // ahora que si hay un lente se puede hablar de
                                                    // distancia de un lente a otro2
                }
            }
        
        };
        
        addLente.addActionListener(oyenteLente);
        botPanel.add(addLente);
        
        //Add  Objeto
        addObject = new JButton();
        addObject.setBounds(650,50,110,20);
        addObject.setText("Add Objeto");
        
        ActionListener oyenteObjeto = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               //topPanel.remove(Objeto);
               // al agregar el objeto lo ponemos
               if(textoObjeto.getText().matches("-?\\d+(\\.\\d+)?") && alturaObjeto.getText().matches("-?\\d+(\\.\\d+)?")){
                   
                   if(nLente > 0) Objeto.setBounds(arrayLentes[0].getX()-(int)Float.parseFloat(textoObjeto.getText())*5,157,25,110);
                   else Objeto.setBounds((int)lentePosIni-(int)Float.parseFloat(textoObjeto.getText())*5,157,25,110);
                   
                   topPanel.add(Objeto);              
               
               // y obviamente aparece la imagen SI HAY ALGUNA LENTE
               if( nLente != 0  ){ 
                    topPanel.add(imagen); // falta en la posicion correcta
               }
               
              // topPanel.repaint(); 
               addObject.setEnabled(false); //y ya no se permite mas objetos           
               textoObjeto.setText("");
               alturaObjeto.setText("");
               //Cuando se usa un remove se usa ambos.
               topPanel.revalidate();
               repaint();
               } 
            }    
        };
        
        addObject.addActionListener(oyenteObjeto);
        botPanel.add(addObject);
    }
    
    public void paint(Graphics g){
        super.paint(g);  //para superponer y no generar conflicto con JButton
   //    g.drawLine(0, getHeight()/2-30, getWidth(), getHeight()/2-30); // seguimiento con ventana
        g.setColor(Color.black);
        g.drawLine(0, 280, getWidth(), 280); 
        
       
        if( (nLente > 0) && (Objeto.getParent() == topPanel)){      
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(4));
        g2.setColor(PURPLE);  
        g2.drawLine(Objeto.getX()+20, Objeto.getY()+48, arrayLentes[0].getX()+20, Objeto.getY()+48); //estaba 600, 280
        encontrarImagen();
        g2.drawLine(arrayLentes[0].getX()+20, Objeto.getY()+48,(int)Xtemp,400);
        }
       
    }
    
    private void encontrarImagen(){ //modo simple (p y q tal como estan falta cuando p o q tan de otros laos)
        
        Xtemp = 1/(2); // no guardamos ni foco ni p entonces falta corregir esto
    }
}

