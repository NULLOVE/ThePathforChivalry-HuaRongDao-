import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class Hrd extends JFrame
{     
	   private final int MaxStep=1000;
	   private int[][] a;           //全局数组,方块映射到些数组。
	   private int[][] recorder;    //记录走法的数组。只有当你走成功后才记录。
       private int totalStep;
       private boolean stop=false;  // to stop the thread
       private int speed=1000;
    
	   private JPanel jw = new JPanel(new GridLayout(5,1));//用于摆放LOGO标志，步数等
	   private Border border = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148,145,140));
      
	   AButton[] bt = new AButton[10];
	   JLabel jstep = new JLabel(" 第 0 步");
      
       MenuBar mym=new MenuBar();
       Menu play=new Menu("游戏"),
	   demo=new Menu("演示"),
       help=new Menu("帮助");
       MenuItem play_new_game=new MenuItem("重新开始"),
                 play_choose=new MenuItem("选择游戏"),
                 play_save=new MenuItem("保存游戏"),
                 play_import=new MenuItem("导入游戏"),
                 play_exit=new MenuItem("退出");
       MenuItem demo_zfgo=new MenuItem("走法演示"),		
	            demo_stop=new MenuItem("停止");
       Menu demo_shudu=new Menu("速度调节");
       CheckboxMenuItem[] mi_speed = new CheckboxMenuItem[4];
       String s[] = {"1","2","3","4"};      
       MenuItem help_topic=new MenuItem("相关主题"),
                help_about=new MenuItem("关于...");

  	public Hrd(){
               this.setSize(500,550);
	           setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               creat_menu();
	           Container contentPane = getContentPane();
               contentPane.setBackground(Color.white );
  	           contentPane.setLayout(null);
               setTitle("标准华容道2004 ");
               setResizable(false);
               Toolkit  kit = Toolkit.getDefaultToolkit();
               Dimension screenSize = kit.getScreenSize();
               setLocation((screenSize.width-getSize().width)/2,(screenSize.height-getSize() .height )/2);
  	    
		
			   bt[0] = new AButton(1,"马超",1,2,0,0);
		       bt[0].setBackground(Color.BLUE);
               contentPane.add(bt[0]);

		       bt[1] = new AButton(2,"曹操",2,2,1,0);
		       bt[1].setBackground(Color.RED);
               contentPane.add(bt[1]);


		       bt[2] = new AButton(3,"赵云",1,2,3,0);
		       bt[2].setBackground(Color.BLUE);
		       contentPane.add(bt[2]);

		       bt[3] = new AButton(4,"张飞",1,2,0,2);
		       bt[3].setBackground(Color.BLUE);
               contentPane.add(bt[3]);

               bt[4] = new AButton(5,"黄忠",1,2,3,2);
		       bt[4].setBackground(Color.BLUE);
               contentPane.add(bt[4]);
                               //ID号,名称,宽,高,位置-X,Y
		       bt[5] = new AButton(6,"关羽",2,1,1,2);  
		       bt[5].setBackground(Color.BLUE);
               contentPane.add(bt[5]);

		       bt[6] = new AButton(7,"卒",1,1,0,4);
		       bt[6].setBackground(Color.YELLOW);
               contentPane.add(bt[6]);

		       bt[7] = new AButton(8,"卒",1,1,1,3);
		       bt[7].setBackground(Color.YELLOW);
               contentPane.add(bt[7]);

		       bt[8] = new AButton(9,"卒",1,1,2,3);
		       bt[8].setBackground(Color.YELLOW);
               contentPane.add(bt[8]);

		       bt[9] = new AButton(10,"卒",1,1,3,4);
		       bt[9].setBackground(Color.YELLOW);
           	   contentPane.add(bt[9]);

               showMyLogo();
               contentPane.add(jw);

               setArr(5,4);   //初始化棋盘
		}

        private void creat_menu(){
              mym.add(play);
	          mym.add(demo);
              mym.add(help);

              play.add(play_new_game);
              play.add(play_choose);
              play.addSeparator() ;
              play.add(play_save);
              play.add(play_import);
              play.addSeparator();
              play.add(play_exit);

              demo.add(demo_zfgo);
	          demo.add(demo_shudu);
              demo.add(demo_stop);
              
      for(int i = 0; i<s.length;i++){

			mi_speed[i] = new CheckboxMenuItem(s[i]);
			demo_shudu.add(mi_speed[i]);

                        mi_speed[i].addItemListener(new ItemListener(){
                        public void itemStateChanged(ItemEvent ie){
                                   int i=0;
                                 for(int j=0;j<s.length;j++) {
                                    mi_speed[j].setState(false);                           
                                    if(ie.getSource() == mi_speed[j]) i=j;
                                 }
                                 mi_speed[i].setState(true);
                                 speed=1000-i*300;                       
                          }                         
                         });				
		}

              help.add(help_topic);
              help.add(help_about);

              setMenuBar(mym) ;

             play_new_game.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
					reset();
                }
        });

  demo_zfgo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
				  reset();
				  ShowZF szf = new ShowZF();
				  stop=false;
				  szf.start();
                }
        });

  demo_stop.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
				  stop=!stop;
				}
  });
        play_exit.addActionListener(new ActionListener(){
              public void actionPerformed(ActionEvent ea){
                  System.exit(0) ;
              }
        });
        help_about.addActionListener(new ActionListener(){
                   public void actionPerformed(ActionEvent ea){
                     JOptionPane.showMessageDialog(null,"jAllen wuhan Email:jpc_27@163.com");
                   }
             });

           help_topic.addActionListener(new ActionListener(){
                   public void actionPerformed(ActionEvent ea){
                     JOptionPane.showMessageDialog(null,"可以用鼠标也可以用光标键移动方块,当曹操移动到底线的中部,你就获胜了");
                   }
             });
              try{  //
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(this) ;
              }catch(Exception e){}


      }
      void showMyLogo(){
                          jw.setSize(100,550) ;
                          jw.setLocation(400,0);
                          jw.setBackground(new Color(220,230,230));
				          jw.setBorder(border);

                         ImageIcon icon=new ImageIcon("javalogo.gif");
                         JButton jb=new JButton(icon);
						 jb.setSize(60,60);
	                     Font ft1 = new Font("隶书",0,27);
						 Font ft2 = new Font("楷书",0,21);
                        
			             JLabel myLogo2=new JLabel(" 蒋 程");
		   	          
                          
							 jstep.setSize(90,30);
							 jstep.setFont(ft2);
                             myLogo2.setSize(100,50) ;
							 myLogo2.setFont(ft1);
                        	
                             jw.add(jstep);
                             jw.add(jb);
							 jw.add(new JLabel(""));
                             jw.add(myLogo2);
                             jw.add(new JLabel("     武汉2004"));
						  }

 void reset(){
                 bt[0].setLocation(0,0);
                 bt[0].x=0;
                 bt[0].y=0;
                 bt[1].setLocation(100,0);
                 bt[1].x=1;
                 bt[1].y=0;
                 bt[2].setLocation(300,0) ;
                 bt[2].x=3;
                 bt[2].y=0;
                 bt[3].setLocation(0,200);
                 bt[3].x=0;
                 bt[3].y=2;
                 bt[4].setLocation(300,200);
                 bt[4].x=3;
                 bt[4].y=2;
                 bt[5].setLocation(100,200);
                 bt[5].x=1;
                 bt[5].y=2;
                 bt[6].setLocation(0,400) ;
                 bt[6].x=0;
                 bt[6].y=4;
                 bt[7].setLocation(100,300);
                 bt[7].x=1;
                 bt[7].y=3;
                 bt[8].setLocation(200,300);
                 bt[8].x=2;
                 bt[8].y=3;
                 bt[9].setLocation(300,400) ;
                 bt[9].x=3;
                 bt[9].y=4;
                 setArr(5,4);   //初始化棋盘

 }

       void setArr(int y,int x){
         a=new int[y][x];
         for(int i=0;i<y;i++)     //构造棋盘
          for(int j=0;j<x;j++)
                 a[i][j]=1;
             a[y-1][1]=0;
             a[y-1][2]=0;
             totalStep=0;
         recorder=new int[MaxStep][2];
        }

private class ShowZF extends Thread  //内部类的用武之地,因为外部类的属性它都能看见,
{     
	private int totalStep;
                             //所以免除了参数传递之麻烦!
	public void run(){       //继承自Thread,所以必须实现run接口
	   FileReader fin=null;
       BufferedReader in=null;
       String line=null;

                            try{
                              fin=new FileReader("华容道走法1.txt");
                              in=new BufferedReader(fin);
                              line=in.readLine();
                              totalStep=0;
		 while(line!=null&&!stop){
                              int index=line.indexOf(',');
                              String a=line.substring(0,index) ;
                              String b=line.substring(index+1,line.length() ) ;
                              int id=Integer.parseInt(a) ;
                              int idr=Integer.parseInt(b) ;

                      try
                       {
                           sleep(speed);
                       }
                       catch(InterruptedException ie) //捕获异常
                       {  }     
					      
							switch(idr){
							  case 0:
                                 bt[id-1].setLocation(bt[id-1].getX() ,bt[id-1].getY() +100);
                                 break;
							  case 1:
                                 bt[id-1].setLocation(bt[id-1].getX()-100 ,bt[id-1].getY()) ;
                                 break;
							  case 2:
                                 bt[id-1].setLocation(bt[id-1].getX() ,bt[id-1].getY()-100) ;
                                 break;                
							  case 3:
                                 bt[id-1].setLocation(bt[id-1].getX()+100 ,bt[id-1].getY()) ;
							     break;
							  default:
								 JOptionPane.showMessageDialog(null,"走法错误!演示中止!");
							     return;
							     							
							} 
                                 totalStep++;
                                 jstep .setText("第 "+String.valueOf(totalStep) +" 步 ") ;
                                 line = in.readLine();

                              }
                            in.close();
                            fin.close();

                            JOptionPane.showMessageDialog(null,"演示结束!");

                            } catch(IOException ioe){JOptionPane.showMessageDialog(null, ioe.toString(), ioe.toString(), JOptionPane.ERROR_MESSAGE);}
                         finally{}
	}
}

 public static void main (String[] args){
                JFrame mybrick = new Hrd();
                mybrick.show();

}

//内部类  AButton
 
 private class AButton extends JButton implements KeyListener,MouseListener{
       
		int ID;
        int x,y;  //location 数组中的位置
        int width,height;  // shape
        int dir=0;
        int lastDir=0;

	public AButton(int i,String s,int w,int h,int xx,int yy){
		       
			   super();
               this.setText(s);
		       this.setSize(w*100,h*100);

                ID=i;
                width=w;
                height=h;
                x=xx;
                y=yy;

                this.setLocation(xx*100,yy*100);
	        	addKeyListener(this);
                addMouseListener(this);

	}
     public int getLocX(){return x;}
     public int getLocY(){return y;}
     public int getW(){return width;}
     public int getH(){return height;}
     public int getID(){return ID;}

     public void recZf(){  //记录华容道的走法
           FileWriter  fout;
                   BufferedWriter  out;
                   String line="";
                    try{
                    fout = new FileWriter("华容道走法.txt");
                    out = new BufferedWriter(fout);
                    for(int i = 0; i < totalStep; i++)
                    {
                            line=String.valueOf(recorder[i][0])+","+String.valueOf(recorder[i][1])+'\n';
                            out.write(line,0,line.length());

                  }
                    out.close();
                   fout.close();

                   }catch(IOException ioe){}


     }
     public void mouseClicked(MouseEvent e){

       int locx,locy,width,height,id;

              id=this.getID() ;
              locx=this.getLocX();  //获得数组中的相应位置
              locy=this.getLocY();
              width=this.getW();
              height=this.getH();

                   if(id==2&&locx==1&&locy+height>=4){ //曹操已到底线,success!
                      if(!judge(locx,locy,width,height,0))
                      {
                        showPassed();
                       }
                   }

                   if(id==2&&locx==0&&locy+height>4){ //曹操已到底线,success!
                     if(!judge(locx,locy,width,height,3))
                  {
                      showPassed();
                  }
               }

               if(id==2&&locx==2&&locy+height>4){ //曹操已到底线,success!
                    if(!judge(locx,locy,width,height,1))
                    {
                       showPassed();
                    }

                 }

                       //走法试探
                       if(judge(locx,locy,width,height,dir)){
                            dir=(dir+1)%4;
                           if(judge(locx,locy,width,height,dir)) {
                             dir=(dir+2)%4;
                             if(judge(locx,locy,width,height,dir)){
                               dir = (dir +3) % 4;
                               if (judge(locx, locy, width, height, dir))
                                 return;
                               }
                             }
                           }
             
                      recorder[totalStep][0]=id;  //记录走法
                      recorder[totalStep][1]=dir;
                   switch(dir){
                    case 0:
                      this.setLocation(this.getX(),(this.getY() +100));
                      showStep();
                      return;

                    case 1:
                      this.setLocation((this.getX()-100),this.getY());
                     showStep();
                     return;

                    case 2:
                      this.setLocation(this.getX(),(this.getY()-100));
                     showStep();
                     return;

                    case 3:
                      this.setLocation((this.getX()+100),this.getY());
                      showStep();
                      return;

                   }
     }

     public void mouseEntered(MouseEvent e){}
     public void mouseExited(MouseEvent e){}
     public void mousePressed(MouseEvent e){}
     public void mouseReleased(MouseEvent e){}

     public void mouseDragged(MouseEvent e){
        
            int locx,locy,width,height,id;

              id=this.getID() ;
              locx=this.getLocX();  //获得数组中的相应位置
              locy=this.getLocY();
              width=this.getW();
              height=this.getH();

                   if(id==2&&locx==1&&locy+height>=4){ //曹操已到底线,success!
                      if(!judge(locx,locy,width,height,0))
                      {
                        showPassed();
                       }
                   }

                   if(id==2&&locx==0&&locy+height>4){ //曹操已到底线,success!
                     if(!judge(locx,locy,width,height,3))
                  {
                      showPassed();
                  }
               }

               if(id==2&&locx==2&&locy+height>4){ //曹操已到底线,success!
                    if(!judge(locx,locy,width,height,1))
                    {
                       showPassed();
                    }
                 }

                       //走法试探， 为真表示有障碍。
                       if(judge(locx,locy,width,height,dir)){           
                            dir=(dir+1)%4;
                           if(judge(locx,locy,width,height,dir)) {
                             dir=(dir+2)%4;
                             if(judge(locx,locy,width,height,dir)){
                               dir = (dir +3) % 4;
                               if (judge(locx, locy, width, height, dir))
                                 return;
                               }
                             }
                           }
             
                      recorder[totalStep][0]=id;  //记录走法
                      recorder[totalStep][1]=dir;

                   switch(dir){
                    case 0:                   //右
                      this.setLocation(this.getX(),(this.getY() +100));
                      showStep();
                      return;

                    case 1:                   //上
                      this.setLocation((this.getX()-100),this.getY());
                     showStep();
                     return;

                    case 2:                     //左
                      this.setLocation(this.getX(),(this.getY()-100));
                     showStep();
                     return;

                    case 3:                   //下
                      this.setLocation((this.getX()+100),this.getY());
                      showStep();
                      return;

                   }

        }

       public boolean judge(int x,int y,int w,int h,int direct){  //x--横向,y--纵向
         switch (direct){
         case 0:  //down
           if(y+h>=5)return true;
           for(int i=0;i<w;i++)
             if(a[y+h][x+i]==1)return true;  //有障碍
           for(int i=0;i<width;i++)   //无障碍，更新数组a[][]
                              { a[y+h][x+i]=1;
                                a[y][x+i]=0;    }
                 this.y++;        //更新在数组中的位置
                return false;     //false表示无障碍
         case 1:  //left
           if(x-1<0)return true; //表明越界．
           for(int i=0;i<h;i++)
           if(a[y+i][x-1]==1)return true;
            for(int i=0;i<h;i++)
              { a[y+i][x-1]=1;
                a[y+i][x+w-1]=0;
              }
              this.x--;
             return false;
         case 2:  //up
           if(y-1<0)return true;
           for(int i=0;i<w;i++)
            if(a[y-1][x+i]==1)return true;
           for(int i=0;i<w;i++)
             { a[y-1][x+i]=1;
               a[y+h-1][x+i]=0;
             }
              this.y--;
               return false;
         case 3:  //right
           if(x+w>=4) return true;
           for(int i=0;i<h;i++)
            if(a[y+i][x+w]==1)return true;
           for(int i=0;i<h;i++)
            { a[y+i][x+w]=1;
              a[y+i][x]=0;
            }
            this.x++;
              return false;
              default:
         }
         return false;
       }


	public void keyPressed(KeyEvent e){

                int locx,locy,width,height,id;

                id=this.getID() ;
                locx=this.getLocX();
                locy=this.getLocY();
                width=this.getW();
                height=this.getH();

		int keyCode = e.getKeyCode();	//得到按键扫描码

                switch(keyCode)
                {
                   case KeyEvent.VK_DOWN :

                     if(id==2&&locx==1&&locy+height>4){ //曹操已到底线,success!
                        if(!judge(locx,locy,width,height,0))
                        {   showPassed();
                         }
                     }

                     if(!judge(locx,locy,width,height,0)){ //无障碍，则移动！
                       recorder[totalStep][0]=id;  //记录走法
                       recorder[totalStep][1]=0;
                         this.setLocation(this.getX(),(this.getY() +100));
                         showStep();
                       }

                        break;
                   case KeyEvent.VK_UP :

                   if(!judge(locx,locy,width,height,2)) {
                     recorder[totalStep][0]=id;  //记录走法
                      recorder[totalStep][1]=2;
                        this.setLocation(this.getX(),(this.getY()-100));

						showStep();
                      }
                         break;
                   case KeyEvent.VK_LEFT :
                     if(id==2&&locx==2&&locy+height>4){ //曹操已到底线,success!
                        if(!judge(locx,locy,width,height,1))
                        {
							showPassed();

                        }

                     }

                     if(!judge(locx,locy,width,height,1)) {
                       recorder[totalStep][0]=id;  //记录走法
                       recorder[totalStep][1]=1;
                       this.setLocation((this.getX()-100),this.getY());

						showStep();

                      }
                      break;
                   case KeyEvent.VK_RIGHT :
                     if(id==2&&locx==0&&locy+height>4){ //曹操已到底线,success!
                        if(!judge(locx,locy,width,height,3))
                        {
							showPassed();

                        }

                     }

                   if(!judge(locx,locy,width,height,3)) {
                     recorder[totalStep][0]=id;  //记录走法
                     recorder[totalStep][1]=3;
                     this.setLocation((this.getX()+100),this.getY());

					  showStep();

                    }
                    break;
              case KeyEvent.VK_ESCAPE :
                 int choosed= JOptionPane.showConfirmDialog(null,"确定要退出吗？","退出华容道",JOptionPane.OK_CANCEL_OPTION ,JOptionPane.WARNING_MESSAGE ) ;
                   if(choosed==JOptionPane.OK_OPTION)  System.exit(0);
                     break;
                    default:
                }

	}

	public void keyTyped(KeyEvent e){}
	public void keyReleased(KeyEvent e){}


    void showStep()   //累加步数,并显示;
	{
	 if(++totalStep>=MaxStep-1){
		 JOptionPane.showMessageDialog(null,"伙计,你累不累?歇一下吧!","I am tired!",1);
         System.exit(0);
					  }
        jstep .setText(" 第"+String.valueOf(totalStep) +"步 ") ;
	}

	void showPassed(){  //过关了!
		  recZf();
		  JOptionPane.showMessageDialog(null,"Congratulations，你过关了！总共"+String.valueOf(totalStep)+"步","恭喜！",1);
          System.exit(0);
	}
  }
}
