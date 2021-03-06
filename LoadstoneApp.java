import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class LoadstoneApp extends JFrame {
	private JPanel contentPane;
	private String urlStr;
	private Toolkit kit = Toolkit.getDefaultToolkit();;
	private Clipboard clipboard = kit.getSystemClipboard();
	final static String KEY_WORD = "open?id=";
	final static String REPLACE_WORD = "uc?export=view&id=";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoadstoneApp frame = new LoadstoneApp();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoadstoneApp() {
		JScrollPane ScPane = new JScrollPane();
		JButton BtnCopyClipBoard = new JButton("コピー");
		JButton BtnDelete = new JButton("消去");
		JButton BtnClear = new JButton("全消去");
		JList list = new JList();
		DefaultListModel model = new DefaultListModel();


		setFont(new Font("游ゴシック", Font.PLAIN, 12));
		setTitle("FF14URL変換器");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 280);
		setResizable(false);
		setAlwaysOnTop(true);

		BtnCopyClipBoard.setFont(new Font("游ゴシック", Font.BOLD, 16));
		BtnDelete.setFont(new Font("游ゴシック", Font.BOLD, 16));
		BtnClear.setFont(new Font("游ゴシック", Font.BOLD, 16));
		list.setFont(new Font("游ゴシック", Font.PLAIN, 9));

		ScPane.getViewport().setView(list);
		ScPane.setBounds(0, 0, 395, 205);
		BtnCopyClipBoard.setBounds(5, 210, 120, 33);
		BtnDelete.setBounds(138, 210, 120, 33);
		BtnClear.setBounds(270, 210, 120, 33);
		list.setModel(model);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		contentPane.add(BtnCopyClipBoard);
		contentPane.add(BtnDelete);
		contentPane.add(BtnClear);
		contentPane.add(ScPane);
		setContentPane(contentPane);

		TimerTask task = new TimerTask() {
			  public void run() {
				  ClipBoardListener(model);
			  }
		};

		Timer timer = new Timer();
		timer.schedule(task,1000,1000);

		BtnCopyClipBoard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = list.getSelectedIndex();
				urlStr = (String)model.getElementAt(index);
				if(!urlStr.equals("")) {
					setClipboardString(urlStr);
				}
			}
		});

		BtnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = list.getSelectedIndex();
				model.remove(index);
			}
		});

		BtnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.clear();
			}
		});
	}

	public void ClipBoardListener(DefaultListModel model) {
		urlStr = GetClipBoardStr();
		if(urlStr == null) {return;}
		if(urlStr.contains(KEY_WORD)) {
			urlStr = ConvertStr(urlStr);
			AddList(urlStr,model);
		}
	}

	public void AddList(String urlStr,DefaultListModel model) {
		if(!ExistsElement(urlStr,model)) {model.addElement(urlStr);}
	};

	public boolean ExistsElement(String urlStr,DefaultListModel model) {
		int iLoop = 0;
		int iCnt = model.getSize();
		String chkStr;

		if(iCnt == 0) {return false;}
		for(iLoop = 0;iLoop < iCnt;iLoop++) {
			chkStr = (String) model.getElementAt(iLoop);
			if(urlStr.equals(chkStr)) {return true;}
		}
		return false;
	}

	public String ConvertStr(String urlStr) {
		urlStr = urlStr.replace(KEY_WORD,REPLACE_WORD);
		return urlStr;
	}

	public String GetClipBoardStr() {
		try {
			return (String) clipboard.getData(DataFlavor.stringFlavor);
		} catch (UnsupportedFlavorException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	public void setClipboardString(String str) {
		StringSelection ss = new StringSelection(str);
		clipboard.setContents(ss, ss);
	}
}
