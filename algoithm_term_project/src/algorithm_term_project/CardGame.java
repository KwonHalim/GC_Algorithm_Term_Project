package algorithm_term_project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class CardGame extends JFrame {
	final int ROWS = 4;
	final int COLS = 4;
	Card[][] cards;
	int cursorRow, cursorCol;
	int moveCount = 0, try_num = 0;
	JLabel try_numLabel;
	int first_num, second_num;
	boolean first = true, second = false;
	int firstclickedrow, firstclickedcol, secondclickedrow, secondclickedcol;
	int resetnum, initialboard[][];
	int r, c;
	boolean end_flag = false;
	boolean entered = false;
	JLabel entered_num, Hint, clicked_number;
	Font font1 = new Font("돋움", Font.BOLD, 30);
	Font font2 = new Font("돋움", Font.PLAIN, 20);
	public CardGame() {
	    cards = new Card[ROWS][COLS];
	    initializeCards();

	    JPanel gridPanel = new JPanel(new GridLayout(ROWS, COLS));
	    for (int i = 0; i < ROWS; i++) {
	        for (int j = 0; j < COLS; j++) {
	            JButton button = cards[i][j].getButton();
	            button.setFont(font1);  // 각 버튼에 폰트 설정
	            gridPanel.add(button);
	        }
	    }
	    
	    // Set the title for the JFrame
	    setTitle("카드 짝 맞추기 게임");
	    
	    //설명 글자 font
	    Font middleSize=new Font("돋움", Font.PLAIN,15);
	    
	    // 설명 label을 JFrame 상단 패널에 추가
	    JLabel descriptionLabel = new JLabel("게임 설명: 이동 횟수 내에 모든 카드의 짝을 맞춰보세요(방향기 : 한칸 이동, ctrl + 방향키 : 카드가 있는 곳까지 방향키 방향으로 이동 없으면 끝까지.");
	    descriptionLabel.setFont(middleSize);
	    JPanel descriptionPanel = new JPanel();
	    descriptionPanel.add(descriptionLabel);
	    add(descriptionPanel, BorderLayout.NORTH);

	    gridPanel.setFocusable(true);
	    gridPanel.addKeyListener(new ArrowKeyListener());
	    add(gridPanel, BorderLayout.CENTER);  // 중앙에 그리드 패널 추가

	    try_numLabel = new JLabel("Try:" + try_num);
	    entered_num = new JLabel("Input number:" + moveCount);
	    Hint = new JLabel("HINT: Soon open");
	    clicked_number = new JLabel("Choosed:");
	    clicked_number.setFont(font2);
	    try_numLabel.setFont(font2);
	    Hint.setFont(font2);
	    entered_num.setFont(font2);
	    // SOUTH에 두 레이블을 추가
	    JPanel southPanel = new JPanel(new GridLayout(1, 3));
	    southPanel.add(try_numLabel);
	    southPanel.add(entered_num);
	    southPanel.add(clicked_number);
//	    southPanel.add(Hint);
	    add(southPanel, BorderLayout.SOUTH);

	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    pack();
	    setLocationRelativeTo(null);
	    setVisible(true);
	}



	void startlocation(int r, int c) {
		this.r = r;
		this.c = c;
		setInitialCursorPosition(r, c);
	}

	private void resetGame() {
		// 뒤집혀진 카드들 다시 뒤집기
		// Restore the initial state of the board
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				cards[i][j].setValue(initialboard[i][j]);
				cards[i][j].getButton().setBackground(UIManager.getColor("Button.background"));
			}
		}

		// 커서 위치 초기 위치로 이동
		setInitialCursorPosition(r, c);
		System.out.println("Game reset!");
		try_num++;
		try_numLabel.setText("Try:" + try_num);
		moveCount = 0; // 움직임 횟수 초기화
		first_num = -1;
		second_num = -1;
		firstclickedrow = -1;
		firstclickedcol = -1;
		secondclickedcol = -1;
		secondclickedrow = -1;
		first = true;
		entered_num.setText("Input number:0");
		if(try_num > 2) {
			Hint.setText("Minimum number is " + resetnum);
			// 저장되었던 값들 모두 초기화
		}
	}

	void saveinitialboardgame(int values[][]) {
		initialboard = values;
	}

	///
	void setBoardValues(int[][] values) {
		if (values.length != ROWS || values[0].length != COLS) {
			throw new IllegalArgumentException("Invalid dimensions for the values array");
		}

		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				cards[i][j].setValue(values[i][j]);
			}
		}
		saveinitialboardgame(values);
	}
	///

	private void highlightCursorPosition() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				cards[i][j].getButton().setBackground(UIManager.getColor("Button.background"));
			}
		}
		cards[cursorRow][cursorCol].getButton().setBackground(Color.RED);
	}
	///

	void setInitialCursorPosition(int r, int c) {
		cursorRow = Math.max(0, Math.min(r, ROWS - 1));
		cursorCol = Math.max(0, Math.min(c, COLS - 1));
		cards[r][c].getButton().setBackground(Color.red);
		// cards[cursorRow][cursorCol].getButton().setBackground(Color.RED);
	}

	private void initializeCards() {
		int cardValue = 1;

		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				cards[i][j] = new Card(cardValue);
				cardValue++;
			}
		}
	}
    private void initializeCardsWithImages() {
        int cardValue = 1;

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                cards[i][j] = new Card(cardValue);
                cardValue++;
            }
        }
    }
    
	private class Card {
		private int value;
		private boolean faceUp;
		private JButton button;

		public Card(int value) {
			this.value = value;
			this.faceUp = false;

			button = new JButton();
			button.setPreferredSize(new Dimension(175, 180));

			////
			updateButton();
			////
		}

		/////
		// Inside the Card class
		public void setValue(int value) {
			this.value = value;
			updateButton();
		}

		///

		public JButton getButton() {
			return button;
		}

		public int getValue() {
			return value;
		}

		public boolean isFaceUp() {
			return faceUp;
		}

		public void flip() {
			faceUp = !faceUp;
			updateButton();
		}

        private void updateButton() {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (faceUp) {
                        // Load image for face-up state
                        String imagePath = "C:\\photo\\" + value + ".jpg"; // Replace with your image path
                        ImageIcon icon = new ImageIcon(imagePath);
                        button.setIcon(icon);
                    } else {
                        // Load image for face-down state
                        String imagePath = "C:\\photo\\" + value + ".jpg"; // Replace with your back image path
                        ImageIcon icon = new ImageIcon(imagePath);
                        button.setIcon(icon);
                    }
                }
            });
        }
		public int getnumber() {
			return value;
		}
	}

	private class ArrowKeyListener implements KeyListener {
		private boolean ctrlPressed = false;

		@Override
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			switch (keyCode) {
			case KeyEvent.VK_UP:
				moveCount++;
				moveCursor(-1, 0);
				break;
			case KeyEvent.VK_DOWN:
				moveCount++;
				moveCursor(1, 0);
				break;
			case KeyEvent.VK_LEFT:
				moveCount++;
				moveCursor(0, -1);
				break;
			case KeyEvent.VK_RIGHT:
				moveCount++;
				moveCursor(0, 1);
				break;
			case KeyEvent.VK_ENTER:
				moveCount++;
				onCardClick();
				break;
			case KeyEvent.VK_CONTROL:
				ctrlPressed = true;
				break;
			}
			if (moveCount >= resetnum) {
				if (!end_flag) {
					JOptionPane.showMessageDialog(null, "게임 실패ㅠㅠ");
				}
				resetGame(); // 초기화 메서드 호출
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
				ctrlPressed = false;
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}

		private void moveCursor(int rowChange, int colChange) {

			if (ctrlPressed) {
				// Ctrl 키가 눌린 상태에서 화살표 키를 누르면 끝까지 이동
				cursorRow = Math.max(0, Math.min(cursorRow + rowChange, ROWS - 1));
				cursorCol = Math.max(0, Math.min(cursorCol + colChange, COLS - 1));
				cards[cursorRow - rowChange][cursorCol - colChange].getButton()
						.setBackground(UIManager.getColor("Button.background"));
				//while != 0이라고 조건을 넣으니 현재 위치도 0이 아닌 경우에는 아예 움직이지 않는 경우가 발생하였음, 그렇기에
				//이동할 위치를 먼저 가보고, 즉 현재 위치를 탈출한 이유에 이동이 가능함. 이는
				//CardSolve의 nr,nc개념과 같다.
				while (cursorRow + rowChange >= 0 && cursorRow + rowChange < ROWS && cursorCol + colChange >= 0
						&& cursorCol + colChange < COLS) {
					if (cards[cursorRow][cursorCol].getnumber() != 0) {
						break;
					}
					cards[cursorRow][cursorCol].getButton().setBackground(UIManager.getColor("Button.background"));
					// Reset the color of the previous cursor position
					cursorRow += rowChange;
					cursorCol += colChange;
				}
				cards[cursorRow][cursorCol].getButton().setBackground(Color.RED); // Set the new cursor position to red
				entered_num.setText("Input number:" + moveCount);
			}

			else {
				// Ctrl 키가 눌리지 않은 상태에서는 일반적인 이동 로직
				int newRow = Math.max(0, Math.min(cursorRow + rowChange, ROWS - 1));
				int newCol = Math.max(0, Math.min(cursorCol + colChange, COLS - 1));
				cards[cursorRow][cursorCol].getButton().setBackground(UIManager.getColor("Button.background")); // Reset the color of the previous cursor position
				cursorRow = newRow;
				cursorCol = newCol;
				cards[cursorRow][cursorCol].getButton().setBackground(Color.RED); // Set the new cursor position to red
				entered_num.setText("Input number:" + moveCount);
			}
			System.out.println(moveCount);
		}
	}

	private void onCardClick() {
		Card clickedCard = cards[cursorRow][cursorCol];
		System.out.println(moveCount);
		if (first) {
			first_num = clickedCard.getnumber();
			clickedCard.getButton().setBackground(Color.yellow);
			first = false;
			System.out.println("First number is " + first_num);
			firstclickedrow = cursorRow;
			firstclickedcol = cursorCol;
			entered = true;
			clicked_number.setText("Choosed: " + Integer.toString(first_num));
			entered_num.setText("Input number:" + moveCount);
		} else {
			second_num = clickedCard.getnumber();
			System.out.println("Second number is " + second_num);
			secondclickedrow = cursorRow;
			secondclickedcol = cursorCol;
			clicked_number.setText("Choosed: " + Integer.toString(second_num));
			entered_num.setText("Input number:" + moveCount);
			if (first_num == second_num) {
				if ((firstclickedrow == secondclickedrow) && (firstclickedcol == secondclickedcol)) {
					System.out.println("Same Position!");
					first_num = -1;
					second_num = -1;
					firstclickedrow = -1;
					firstclickedcol = -1;
					secondclickedcol = -1;
					secondclickedrow = -1;
					first = true;
					cards[firstclickedrow][firstclickedcol].getButton().setBackground(UIManager.getColor("Button.background"));
					entered_num.setText("Input number:" + moveCount);
					//만약 같은 위치의 카드를 고른다면 룰 위반..
				} else {
					clickedCard.getButton().setBackground(Color.yellow);
					System.out.println("Match! Numbers are the same.");
					cards[firstclickedrow][firstclickedcol].setValue(0);
					cards[secondclickedrow][secondclickedcol].setValue(0);
					first = true;
					game_end(cards);
					entered_num.setText("Input number:" + moveCount);
					//같은 위치가 아닐 때 같은 숫자라면 OK
				}
			} else {
				System.out.println("No match. Numbers are different.");
				first_num = -1;
				second_num = -1;
				firstclickedrow = -1;
				firstclickedcol = -1;
				secondclickedcol = -1;
				secondclickedrow = -1;
				first = true;
				cards[firstclickedrow][firstclickedcol].getButton().setBackground(UIManager.getColor("Button.background"));
				//다른 카드를 골랐을 때
			}
		}
	}

	void resetVal(int reset) {
		this.resetnum = reset;
	}

	void game_end(Card cards[][]) {
		end_flag = true; // 초기값을 true로 설정

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (cards[i][j].getValue() != 0) {
					end_flag = false; // 하나라도 0이 아닌 카드가 있으면 false로 변경하고 반복문 종료
					break;
				}
			}
			if (!end_flag) {
				break;
			}
		}

		if (end_flag) {
			JOptionPane.showMessageDialog(null, "게임 성공!");
		}
	}
}
