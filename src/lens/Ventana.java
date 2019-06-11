package lens;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.Icon;
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
    
    // Componentes en pantalla.
    private JTextField textoFoco;
    private JTextField textoDisLente;
    private JTextField textoObjeto;
    private JTextField alturaObjeto;
    private JButton addObject;
    private JButton addLente;
    
    // Para cargar imagen a ser escalada
    private ImageIcon imgObjeto;
    private ImageIcon imgObjetoInverso;
    private ImageIcon imgImagenInverso;
    private ImageIcon imgImagen;
    
    // Para el escalado
    private JLabel Objeto;
    private JLabel ObjetoInverso;
    private JLabel ImagenInverso;
    private JLabel imagen;
    private JLabel[] arrayLentes = new JLabel[23];

    // Variables para controlar lentes.
    private int nLente = 0;  // indica cuantos lentes hay. (n-1 seria la posicion)
    private int lentei = 0;  // usado para identificar lente que estamos interactuando.
    

    // Menu para eliminar objeto y lente con pop y pop2 respectivamente
    private JPopupMenu popup;
    private JPopupMenu popup2;
    
    private float lentePosIni = 450;
    private float posAcumuladoX = 450;
    private float lentePosDx  = 0;
    
    private float objetoPosX;
    private float objetoPosY;
    private float[] arrayFocos = new float[23];
    
    private float Xtemp = 0;
    private float Ytemp = 0;
    
    private int localObjetoY;
    
    //para la ecuacion de fabricante
    int A,O,F,Y,P; 
    
    //RGB Colors
    public static final Color PURPLE = new Color(137,18,196);
    public static final Color GREEN = new Color(102,204,0);
    public static final Color ORANGE = new Color(255,128,0);
  
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

        
        imgObjeto = new ImageIcon("objeto.png");
        Objeto = new JLabel();
        
        imgObjetoInverso = new ImageIcon("objetoInverso.png");
        ObjetoInverso = new JLabel();
        
        imgImagen = new ImageIcon("imagen.png");
        imagen = new JLabel();
        
        imgImagenInverso = new ImageIcon("imagenInverso.png");
        ImagenInverso = new JLabel();
        // COMENZAR AQUI:
        // ESCALADO CORRECTO DE IMAGEN CASO CUANDO CON LENTE CONVERG OBJETO ESTA ENTRE F Y CENTRO 
        // FALTA ARREGLAR CASI DIVERGENTE MAL PARAMETROS DIVISION ENTRE 0
        // ARREGLAR LOS CASOS SEGUN :
        // LENTE CONVERGENTE.-
        //                  OBJETO INVERSO CUANDO MAS LEJOS DEL FOCO Y CASO CONTRARIO OBJETO NORMAL
        //                  ESTO SE PUEDE CORREGIR CON EL SIGNO PARA GENERALIZARLO Y NO HACER 2 CASOS
        // LENTE DIVERGENTE.-
        //                  SIEMPRE OBJETO PARADO (AUMENTO M ES +)
        // Y Q LOS PARAMETROS SEAN GLOBALES 
        // PARA GENERALIZAR EN PAINT(GRAPHICS G) PODEMOS HACER FUNCIONES PARA GENERALIZAR LOS ESPEJOS
        // FALTA GENERALIZAR PARA CUANDO USEMOS DOS ESPEJOS BORREMOS EL PRIMERO Y AGREGAMOS OBJETO
        // Y CUANDO BORREMOS UN ESPEJO SE ACTUALIZE EL SIGUIENTE BIEN FALTA ESO.
        
        // AHORA FALTARIA MOSTRAR LOS DATOS 
        // TENEMOS QUE DIFERENCIAR ENTRE LOS DATOS FLOAT Y ENTERO QUE SE USAN PARA GRAFICAR

        // OPCIONAL : AQUI FALTA AGREGAR AOP
        // TENEMOS Q DEFINIR UNA CLASE CUYO OBJETOS TENGAN LOS MISMO METODOS
        // ESTE METODO SERA COLOCADO EN UN ASPECTO PARA PROGRAMARLO DE MANERA TRANSVERSAL
           
        //JPopMenu
        popup = new JPopupMenu();
        ActionListener menuListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                topPanel.remove(Objeto);        // no problem si no existe no aplica
                topPanel.remove(ObjetoInverso); // no problem si noexiste no aplica
                topPanel.remove(imagen);
                topPanel.remove(ImagenInverso);
                if( nLente != 0) ecuacionFabricante();
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
        
        MouseListener oyenteMouse2 = new MouseListener(){
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
                 popup.show(ObjetoInverso, topPanel.getX(), topPanel.getY());
                 }
                topPanel.revalidate();
                repaint(); // para no despintar linea negra de graphics
              }   
        };
        
        Objeto.addMouseListener(oyenteMouse);
        ObjetoInverso.addMouseListener(oyenteMouse2);
     
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
        for( int i = lentei; i < nLente; i++){
            arrayLentes[i] = arrayLentes[i+1];
            arrayFocos[i] = arrayFocos[i+1];
        }
            nLente = nLente - 1;
    }
    
    
    private void iniciarButtons(){
        
        
        // Accion eliminando lente en cuestion
        popup2 = new JPopupMenu();
        ActionListener menuListener2 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                topPanel.remove(arrayLentes[lentei]); // Solo se quita de pantalla
                reducirLente(); // quitarlo del array
                
                if(nLente == 0){ // Caso eliminamos y no queda lentes
                    textoDisLente.setEnabled(false);
                    topPanel.remove(imagen);
                    topPanel.remove(ImagenInverso); // remove imagen ya que no hay lentes
                    // reiniciamos posicion por default con las dos variables a usar
                    lentePosDx = 0;
                    posAcumuladoX = lentePosIni;
                    
                }else {  //Caso eliminamos y queda lentes todavia
                    posAcumuladoX = arrayLentes[nLente-1].getX(); // actualizamos posicion acumulada
                    // Esto asegura que siempre se agregara objeto al primer lente sea cual sea pero aun no se usa objetoPosX.
                  //  objetoPosX = arrayLentes[0].getX()-Objeto.getX(); // se actualiza para el primer lente
                    ecuacionFabricante(); //corregimos la nueva ecuacion de fabricante pq hay nueva lente
                    //Actualizamos imagen 
                    if( Xtemp < 0 ){
                    System.out.println("obvio");
                    imagen.setBounds(arrayLentes[0].getX()+10+(int)Xtemp,250, 10 ,-Y);
                    imagen.setIcon(new ImageIcon(imgImagen.getImage().getScaledInstance(10, Y, Image.SCALE_SMOOTH)));
                    topPanel.add(imagen);
                    }
                    else{ System.out.println("xd");
                    ImagenInverso.setBounds(arrayLentes[0].getX()+10+(int)Xtemp,250, 10 ,Y);
                    ImagenInverso.setIcon(new ImageIcon(imgImagenInverso.getImage().getScaledInstance(10, Y, Image.SCALE_SMOOTH)));
                    topPanel.add(ImagenInverso);
                    }
                }

                topPanel.revalidate();  
                repaint();
            
            }
        };
        
        // Menu mensaje al dar right click al lente
        JMenuItem item;
        popup2.add(item = new JMenuItem("Eliminar"));
        item.addActionListener(menuListener2); //al dar click a item se activa action
        
        // Capturando accion del lente
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
                    lentei = Comparar(e.getSource()); // Verifica que lente estamos tocando
                    popup2.show(arrayLentes[lentei], topPanel.getX(), topPanel.getY()); //Aparece mensaje en ese lente
                }
                topPanel.revalidate();
                repaint(); // para no despintar linea negra de graphics
            }   
        };
        
      //  Objeto.addMouseListener(oyenteMouseLente);
        
        //Add Lente button
        addLente = new JButton();
        addLente.setBounds(650,20,110,20);
        addLente.setText("Add Lente"); // Se agrega cada lente a la derecha del ultimo agregado
        
        ActionListener oyenteLente = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
                // Abarca dos casos: ingresa si es el primero ya que textoDisLente esta desactivado
                // y entra si tenemos llenos los dos campos foco y distancia para los demas casos.
                if( (textoFoco.getText().matches("-?\\d+(\\.\\d+)?") && !textoDisLente.isEnabled()) 
                        || (textoFoco.getText().matches("-?\\d+(\\.\\d+)?") && textoDisLente.getText().matches("-?\\d+(\\.\\d+)?"))  ){ 
                    
                    
                    arrayFocos[nLente] = Float.parseFloat(textoFoco.getText());
                    
                    // Caso lente divergente
                    if( Float.parseFloat(textoFoco.getText()) < 0 ){ 
                        arrayLentes[nLente] = new JLabel(new ImageIcon("lented.png"));
                        arrayLentes[nLente].addMouseListener(oyenteMouseLente); // Habilitamos opcion a eliminar
                               
                       // Caso hay lente anterior agregamos a la derecha respecto a la distancia ingresada
                       if( textoDisLente.isEnabled() == true ){  
                           lentePosDx = Integer.parseInt(textoDisLente.getText());                        
                           lentePosDx = lentePosDx*5; // Escalado de 5 para graficar bien              
                       }

                       arrayLentes[nLente].setBounds((int)(posAcumuladoX+lentePosDx),90,25,325); //Nueva posicion
                       posAcumuladoX = posAcumuladoX + lentePosDx; // Actualizamos acumulado para siguiente lente si es q hubiese
                       topPanel.add(arrayLentes[nLente]);
                       nLente = nLente + 1;          
                       topPanel.revalidate();
                       repaint();
                       
                       // Luego de poner el lente si hay objeto entonces agregamos Imagen
                       if( Objeto.getParent() == topPanel ){ 
                            ecuacionFabricante();
                            if( Xtemp < 0 ){
                            imagen.setBounds(arrayLentes[0].getX()+10+(int)Xtemp,130, 10 ,-Y);
                            imagen.setIcon(new ImageIcon(imgImagen.getImage().getScaledInstance(10, -Y, Image.SCALE_SMOOTH)));
                            topPanel.add(imagen);
                            }else{
                            ImagenInverso.setBounds(arrayLentes[0].getX()+10+(int)Xtemp,250, 10 ,Y);
                            ImagenInverso.setIcon(new ImageIcon(imgImagenInverso.getImage().getScaledInstance(10, Y, Image.SCALE_SMOOTH)));
                            topPanel.add(ImagenInverso);
                    }
                       }
                       
                    }else{ // Caso lente convergente
                        
                        arrayLentes[nLente] = new JLabel(new ImageIcon("lentec.png"));
                        arrayLentes[nLente].addMouseListener(oyenteMouseLente);
                           
                       if( textoDisLente.isEnabled() == true ){
                           lentePosDx = Integer.parseInt(textoDisLente.getText());                        
                           lentePosDx = lentePosDx*5;               
                       }

                       arrayLentes[nLente].setBounds((int)(posAcumuladoX+lentePosDx),90,25,325); //Nueva posicion
                       posAcumuladoX = posAcumuladoX + lentePosDx;
                       topPanel.add(arrayLentes[nLente]);
                       nLente = nLente + 1;
                       lentePosDx = lentePosDx+100;
                       topPanel.revalidate();
                       repaint(); 
                       
                       // Luego de poner el lente si hay objeto graficamos la imagen
                       if( Objeto.getParent() == topPanel ){
                            ecuacionFabricante();
                            if( Xtemp < 0 ){
                            imagen.setBounds(arrayLentes[0].getX()+10+(int)Xtemp,130, 10 ,-Y);
                            imagen.setIcon(new ImageIcon(imgImagen.getImage().getScaledInstance(10, -Y, Image.SCALE_SMOOTH)));
                            topPanel.add(imagen);
                            }else{
                            ImagenInverso.setBounds(arrayLentes[0].getX()+10+(int)Xtemp,250, 10 ,Y);
                            ImagenInverso.setIcon(new ImageIcon(imgImagenInverso.getImage().getScaledInstance(10, Y, Image.SCALE_SMOOTH)));
                            topPanel.add(ImagenInverso);
                    }
                       } 
                       
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
        
        //Add  Objetob button
        addObject = new JButton();
        addObject.setBounds(650,50,110,20);
        addObject.setText("Add Objeto"); // Se agrega a la izquierda del primer lente, esté donde esté

        ActionListener oyenteObjeto = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               //topPanel.remove(Objeto);
               // 
               // Agregando objeto 
               if(textoObjeto.getText().matches("-?\\d+(\\.\\d+)?") && alturaObjeto.getText().matches("-?\\d+(\\.\\d+)?")){
                   
                   objetoPosY = Integer.parseInt(alturaObjeto.getText())*6;
                   localObjetoY = (int)objetoPosY;
                   objetoPosX = Float.parseFloat(textoObjeto.getText())*5;
                   
                   if(nLente > 0){ // si hay lentes referenciamos al lente y le agregamos en base al primero
                       
                       // 250 - objetoPosY (con 250 te posicionas en el centro luego corres lo que mide el objeto y listo ubicado en linea
                       if(localObjetoY < 0){ // Caso objeto invertido
                        localObjetoY = -localObjetoY; // para graficar sin errores (segunda parte de bounds es el margen q contiene la imagen)
                        ObjetoInverso.setBounds(arrayLentes[0].getX()-(int)objetoPosX,250, 10 ,localObjetoY);
                        ObjetoInverso.setIcon(new ImageIcon(imgObjetoInverso.getImage().getScaledInstance(10, localObjetoY, Image.SCALE_SMOOTH)));
                        topPanel.add(ObjetoInverso);
                       }else{ // Caso objeto derecho
                       Objeto.setBounds(arrayLentes[0].getX()+7-(int)objetoPosX,250-localObjetoY, 10 ,localObjetoY);
                       Objeto.setIcon(new ImageIcon(imgObjeto.getImage().getScaledInstance(10, localObjetoY, Image.SCALE_SMOOTH)));
                       topPanel.add(Objeto);
                       }
                   }
                   else{ // si no hay lentes agregamos en un lugar definido previamente (lentePosIni)
                       if(localObjetoY < 0){
                        localObjetoY = -localObjetoY; // para graficas sin errores
                        ObjetoInverso.setBounds((int)lentePosIni+7-(int)objetoPosX,250, 10, localObjetoY);
                        ObjetoInverso.setIcon(new ImageIcon(imgObjetoInverso.getImage().getScaledInstance(10, localObjetoY, Image.SCALE_SMOOTH)));
                        topPanel.add(ObjetoInverso);
                       }else{
                       Objeto.setBounds((int)lentePosIni-(int)objetoPosX,250-localObjetoY, 10, localObjetoY);
                       Objeto.setIcon(new ImageIcon(imgObjeto.getImage().getScaledInstance(10, localObjetoY, Image.SCALE_SMOOTH)));
                       topPanel.add(Objeto);
                       }
                   }
                   
                   
               // Caso primero se agrego lente(s) y luego Objeto
               if( nLente != 0 ){
                    ecuacionFabricante();
                    if( Xtemp < 0 ){
                    imagen.setBounds(arrayLentes[0].getX()+10+(int)Xtemp,130, 10 ,-Y);
                    imagen.setIcon(new ImageIcon(imgImagen.getImage().getScaledInstance(10, -Y, Image.SCALE_SMOOTH)));
                    topPanel.add(imagen);
                    }else{
                    ImagenInverso.setBounds(arrayLentes[0].getX()+10+(int)Xtemp,250, 10 ,Y);
                    ImagenInverso.setIcon(new ImageIcon(imgImagenInverso.getImage().getScaledInstance(10, Y, Image.SCALE_SMOOTH)));
                    topPanel.add(ImagenInverso);
                    }
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
        
             
        if( (nLente > 0) && ((Objeto.getParent() == topPanel) || ObjetoInverso.getParent() == topPanel)){      
        Graphics2D g2 = (Graphics2D) g;
        // linea punteada usar dashed en setStroke
        Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0);
        //lineas punteadas  
        //g2.setStroke(dashed); 
        g2.setStroke(new BasicStroke(3));
        
        // Caso convergente : objeto mas lejos que el foco
        if( arrayFocos[0] > 0 &&  (arrayLentes[0].getX() - Objeto.getX() - 3)/5 > arrayFocos[0] ){ 
            g2.setColor(PURPLE); //localObjetoY esta en INT para poder usarlo pa graficos
            g2.drawLine(Objeto.getX()+13, Objeto.getY()+30, arrayLentes[0].getX()+20, Objeto.getY()+30);
            g2.drawLine(arrayLentes[0].getX()+20, Objeto.getY()+30, arrayLentes[0].getX()+20 +(int)arrayFocos[0]*5, 280);
            // 60 para corregir los limites no buenos que da arrayLentes[0].getX();
             // se encuentra p (imagen en X)
           //calculo:
            g2.drawLine(arrayLentes[0].getX()+20 +(int)arrayFocos[0]*5, 280, arrayLentes[0].getX()+20+(int)Xtemp, 280+Y );
            
            g2.setColor(GREEN);
            g2.drawLine(Objeto.getX()+13, Objeto.getY()+31,arrayLentes[0].getX()+20,280);
            g2.drawLine(arrayLentes[0].getX()+20,280,arrayLentes[0].getX()+20+(int)Xtemp, 280+Y );
            
            g2.setColor(ORANGE);
            g2.drawLine(Objeto.getX()+13, Objeto.getY()+31, arrayLentes[0].getX()+20 - (int)arrayFocos[0]*5 ,280);
            //-28 para corregir el limite no bueno que da arrayLentes[0].getX()             
            g2.drawLine(arrayLentes[0].getX()+20 - (int)arrayFocos[0]*5  , 280,  arrayLentes[0].getX()+20 ,280+Y);             
            g2.drawLine( arrayLentes[0].getX()+20 ,280+Y,arrayLentes[0].getX()+20+(int)Xtemp, 280+Y );
        }
         //estaba 600, 280
        else {
            g2.setColor(PURPLE); //localObjetoY esta en INT para poder usarlo pa graficos
            g2.drawLine(Objeto.getX()+13, Objeto.getY()+30, arrayLentes[0].getX()+20, Objeto.getY()+30);
            g2.drawLine(arrayLentes[0].getX()+20, Objeto.getY()+30, arrayLentes[0].getX()+20 +(int)arrayFocos[0]*5, 280);        
            g2.setStroke(dashed);
            g2.drawLine(arrayLentes[0].getX()+20 +(int)arrayFocos[0]*5, 280, arrayLentes[0].getX()+20+(int)Xtemp, 280+Y );
            
            g2.setColor(GREEN);
            g2.setStroke(new BasicStroke(3));
            g2.drawLine(Objeto.getX()+13, Objeto.getY()+31,arrayLentes[0].getX()+20,280);
            g2.drawLine(arrayLentes[0].getX()+20,280,arrayLentes[0].getX()+20-(int)Xtemp, 280-Y ); // para extender linea
            g2.setStroke(dashed);
            g2.drawLine(arrayLentes[0].getX()+20,280,arrayLentes[0].getX()+20+(int)Xtemp, 280+Y );
            
            g2.setColor(ORANGE);
            g2.setStroke(new BasicStroke(3));
            g2.drawLine(Objeto.getX()+13, Objeto.getY()+31, arrayLentes[0].getX()+20 - (int)arrayFocos[0]*5 ,280);
            g2.drawLine(arrayLentes[0].getX()+20 - (int)arrayFocos[0]*5  , 280,  arrayLentes[0].getX()+20 ,280+Y);
            g2.drawLine( arrayLentes[0].getX()+20 ,280+Y,arrayLentes[0].getX()+20-(int)Xtemp, 280+Y ); // para extender linea
            g2.setStroke(dashed);
            g2.drawLine( arrayLentes[0].getX()+20 ,280+Y,arrayLentes[0].getX()+20+(int)Xtemp, 280+Y );
        }
        /* // otra linea para usar
        g2.setStroke(new BasicStroke(3));
        g2.setColor(BLUE);
        g2.drawLine(0, 0, 255, 500);
        */
       
       // g2.drawLine(arrayLentes[0].getX()+20, Objeto.getY()+48,arrayLentes[0].getX()+(int)Xtemp*7,355); //  7 regular imagen
        }
         
    }
    
    private void ecuacionFabricante(){      
       
        // Para calcular geometricamente rayo del foco al lente 
        // O*B/A = Y -> siendo Y la posicion en el eje y del lente donde cae el rayo.
        /*
        A = (arrayLentes[0].getX()+20 - (int)arrayFocos[0]*5) - (Objeto.getX()+13); //A
        F = (int)arrayFocos[0]*5; //F oco
        
        Y = O*F/A;
        P = arrayLentes[0].getX()+20-(Objeto.getX()+13);
        encontrarImagen(F,P); // Xtemp listo para usar */
        O = 250-Objeto.getY(); // O bjeto altura
        F = (int)arrayFocos[0]*5; // Foco
        P = arrayLentes[0].getX()+20-(Objeto.getX()+13);
        encontrarImagen(F,P);
        Y = (int)Xtemp/P;  //primero aumento M ; no ponemos signo menos pq Xtemp ya lleva su signo
        Y = O*Y; // calculamos distancia con aumento
        System.out.println(F+" "+P+" "+Y);
    }
    
    private void encontrarImagen(int foco,int pp){ //modo simple (p y q tal como estan falta cuando p o q tan de otros laos)?
        
        Xtemp = 1/(1/(float)foco-1/(float)pp);  // 1/p + 1/q = 1/f
    }
}

