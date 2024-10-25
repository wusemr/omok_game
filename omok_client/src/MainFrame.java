import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//대기실 기능을 수행하는 인터페이스
public class MainFrame extends JFrame{
    /* Panel */
    JPanel basePanel = new JPanel(new BorderLayout());
    JPanel centerPanel = new JPanel();
    JPanel eastPanel = new JPanel();

    /* Label */
    JLabel roomListL = new JLabel("================ 방 목록 ================");
    JLabel cuListL = new JLabel("======= 접속 인원 =======");

    /* ScrollPane */
    JScrollPane rL_sp;
    JScrollPane cL_sp;

    /* List */
    JList<String> rList = new JList<String>();
    JList<String> cuList = new JList<String>();

    /* Menu */
    JMenuBar mb = new JMenuBar();
    JMenu infoMenu = new JMenu("내정보");
    JMenuItem viewInfo = new JMenuItem("내 정보 보기");
    JMenuItem changeInfo = new JMenuItem("내 정보 바꾸기");

    /* Button */
    JButton viewRanking = new JButton("전적 보기");
    JButton createRoom = new JButton("방 생성하기");
    JButton enterRoom = new JButton("방 입장하기");
    JButton exitGame = new JButton("게임 종료하기");

    String selRoom;		//선택된 방 제목
    String roomName;	//생성할 방 제목

    Client c = null;

    final String croomTag = "CROOM";	//방 생성 기능 태그
    final String eroomTag = "EROOM";	//방 입장 기능 태그
    final String rankTag = "RANK";		//전적 조회 기능 태그
    final String pexitTag = "PEXIT";	//프로그램 종료 기능 태그

    MainFrame(Client _c) {
        c = _c;

        setTitle("대기실");

        /* Menu */
        infoMenu.add(viewInfo);
        infoMenu.addSeparator();
        infoMenu.add(changeInfo);
        mb.add(infoMenu);
        setJMenuBar(mb);

        /* Panel 크기 작업 */
        centerPanel.setPreferredSize(new Dimension(310, basePanel.getHeight()));
        eastPanel.setPreferredSize(new Dimension(180, basePanel.getHeight()));

        /* Label 크기 작업 */
        roomListL.setPreferredSize(new Dimension(290, 20));
        cuListL.setPreferredSize(new Dimension(160, 20));

        /* ScrollPane 크기 작업 */
        rL_sp = new JScrollPane(rList);
        cL_sp = new JScrollPane(cuList);
        rL_sp.setPreferredSize(new Dimension(300, 350));
        cL_sp.setPreferredSize(new Dimension(160, 188));

        /* Button 크기 작업 */
        viewRanking.setPreferredSize(new Dimension(160, 35));
        createRoom.setPreferredSize(new Dimension(160, 35));
        enterRoom.setPreferredSize(new Dimension(160, 35));
        exitGame.setPreferredSize(new Dimension(160, 35));

        /* Panel 추가 작업 */
        setContentPane(basePanel);	//panel을 기본 컨테이너로 설정

        basePanel.add(centerPanel, BorderLayout.CENTER);
        basePanel.add(eastPanel, BorderLayout.EAST);

        centerPanel.setLayout(new FlowLayout());
        eastPanel.setLayout(new FlowLayout());

        centerPanel.add(roomListL);
        centerPanel.add(rL_sp);

        /* eastPanel 컴포넌트 */
        eastPanel.add(cuListL);
        eastPanel.add(cL_sp);
        eastPanel.add(viewRanking);
        eastPanel.add(createRoom);
        eastPanel.add(enterRoom);
        eastPanel.add(exitGame);

        /* MenuItem 이벤트 리스너 추가 */
        MenuItemListener mil = new MenuItemListener();

        viewInfo.addActionListener(mil);
        changeInfo.addActionListener(mil);

        /* Button 이벤트 리스너 추가 */
        ButtonListener bl = new ButtonListener();

        viewRanking.addActionListener(bl);
        createRoom.addActionListener(bl);
        enterRoom.addActionListener(bl);
        exitGame.addActionListener(bl);

        /* Mouse 이벤트 추가 */
        rList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!rList.isSelectionEmpty()) {
                    String[] m = rList.getSelectedValue().split(" : ");
                    selRoom = m[0];
                }
            }
            public void mousePressed(MouseEvent e) { }
            public void mouseReleased(MouseEvent e) { }
            public void mouseEntered(MouseEvent e) { }
            public void mouseExited(MouseEvent e) { }
        });

        setSize(510, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /* Button 이벤트 리스너 */
    class ButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton b = (JButton)e.getSource();

            /* 전적 보기 버튼 이벤트 */
            if(b.getText().equals("전적 보기")) {
                c.rf.setVisible(true);
                System.out.println("[Client] 전적 조회 인터페이스 열림");
                c.sendMsg(rankTag + "//");	//서버에 전적 조회 태그 전송
            }

            /* 방 생성하기 버튼 이벤트 */
            else if(b.getText().equals("방 생성하기")) {
                //생성할 방 제목을 입력받음
                roomName = JOptionPane.showInputDialog(null, "생성할 방 제목을 입력하시오", "방 생성", JOptionPane.QUESTION_MESSAGE);

                if(roomName != null) {	//roomName이 null이 아니면 서버에 "태그//방이름" 형태의 메시지를 전송
                    c.sendMsg(croomTag + "//" + roomName);
                } else {				//roomName이 null이면 방 생성 시도 실패
                    JOptionPane.showMessageDialog(null, "방 제목이 입력되지 않았습니다", "생성 실패", JOptionPane.ERROR_MESSAGE);
                    System.out.println("[Client] 방 생성 오류 : 입력 값이 존재하지 않음");
                }
            }

            /* 방 입장하기 버튼 이벤트 */
            else if(b.getText().equals("방 입장하기")) {
                if(selRoom != null) {	//selRoom이 null이 아니면 서버에 "태그//방이름" 형태의 메시지를 전송
                    c.sendMsg(eroomTag + "//" + selRoom);
                } else {				//selRoom이 null이면 입장 시도 실패
                    JOptionPane.showMessageDialog(null, "입장할 방을 선택해주세요", "입장 실패", JOptionPane.ERROR_MESSAGE);
                    System.out.println("[Client] 방 입장 오류 : 선택 값이 존재하지 않음");
                }
            }

            /* 게임 종료하기 버튼 이벤트 */
            else if(b.getText().equals("게임 종료하기")) {
                System.out.println("[Client] 게임 종료");
                c.sendMsg(pexitTag + "//");		//서버에 프로그램 종료 태그 전송
                System.exit(0);					//프로그램 강제 종료
            }
        }
    }

    /* MenuItem 이벤트 리스너 */
    class MenuItemListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem mi = (JMenuItem)e.getSource();

            /* 내 정보 보기 메뉴 이벤트 */
            if(mi.getText().equals("내 정보 보기")) {
                c.inf.setVisible(true);
                System.out.println("[Client] 회원 정보 조회 인터페이스 열림");
            }

            /* 내 정보 바꾸기 메뉴 이벤트 */
            else if(mi.getText().equals("내 정보 바꾸기")) {
                c.cinf.setVisible(true);
                System.out.println("[Client] 회원 정보 변경 인터페이스 열림");
            }
        }
    }
}