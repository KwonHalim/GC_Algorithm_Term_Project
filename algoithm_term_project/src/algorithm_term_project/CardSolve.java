package algorithm_term_project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class CardSolve {
	class Point {
		int row, col, cnt;// cnt는 조작횟수

		Point(int r, int c, int t) {// 생성자 정의
			row = r;
			col = c;
			cnt = t;
		}

	}

	static final int INF = 987654321;
	public static int[][] input_board;
	int[][] Board;
	static final int[][] D = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

	// 상하좌우 이동 배열
	int bfs(Point src, Point dst) {
		boolean[][] visited = new boolean[4][4];
		// 방문했는지의 여부 확인할 2차원 배열

		Queue<Point> q = new LinkedList<>();
		q.add(src);
		// 큐에 위치 삽입하고 시작
		while (!q.isEmpty()) {
			Point curr = q.poll();
			// 디큐. 현재 위치가 어디인지 확인
			if (curr.row == dst.row && curr.col == dst.col) {
				// 매개변수로 나의 현재 위치와 목표 위치를 확인하였음 지금 조건은 같음
				return curr.cnt;
				// 현재 좌표의 조작횟수 return 즉 curr은 계속해서 나의 위치를 나타내는 Point 변수이기 때문에 현재까지의 나의 조작 횟수를
				// 의미한다.

			}
			for (int i = 0; i < 4; i++) {
				int nr = curr.row + D[i][0], nc = curr.col + D[i][1];
				// 4개의 방향 탐색 nr은 새로운 행으로 이동한다는 뜻, nc는 새로운 열로 이동
				if (nr < 0 || nr > 3 || nc < 0 || nc > 3)
					continue;
				// 만약 지정된 배열을 넘어선다면 이동할 수 없으니 skip
				if (!visited[nr][nc]) {
					// 여기까지 왔다면 이동가능하다는 것이기 때문에 방문하지 않은 배열이라면 방문처리한다.
					visited[nr][nc] = true;
					q.add(new Point(nr, nc, curr.cnt + 1));
					// 큐에 삽입, 방향키를 눌러 이동해서 가능한 좌표를 큐에 삽입한다.
				}
				for (int j = 0; j < 2; j++) {
					// 만약 컨트롤 키를 눌렀다면을 가정하는 것, 최대 이동가능한 것은 2번 왜?
					// 위에서 이미 한번 이동한 경우이기에 또 추가로 더 이동가능한 것은 나머지 2번임
					// 즉 최대 이동횟수는 많아봤자 2번이다.
					if (Board[nr][nc] != 0)
						break;
					// 이미 한번 움직인 것을 가정하는 상태인데, 그곳에 0이 아닌, 즉 카드가 있다면 멈춰야 한다.
					if (nr + D[i][0] < 0 || nr + D[i][0] > 3 || nc + D[i][1] < 0 || nc + D[i][1] > 3)
						// 이동하는데 한번 이상 이동하면 배열의 끝에 위치한다고 해도 더이상의 이동이 불가함
						break;
					// break가 아니라면 계속 이동
					nr += D[i][0];
					nc += D[i][1];
				}
				if (!visited[nr][nc]) {
					visited[nr][nc] = true;
					q.add(new Point(nr, nc, curr.cnt + 1));
				}
			}

		}
		return INF;
	}

	int permutate(Point src) {
		int ret = INF;// 최솟값을 찾기 때문에 첨에는 무한값

		for (int num = 1; num <= 6; num++) {
			// 제거할 카드는 1~6이므로 각각의 카드 삭제할 것 결정
			List<Point> card = new ArrayList<>();
			// ARRAYLIST로 제거할 카드 정보를 저장할 리스트 생성
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if (Board[i][j] == num) {
						// 전체 크기가 작기 때문에 제거할 숫자를 정했다면(위의 for문) 해당 카드 위치 구함-->i행j열에 있는카드
						card.add(new Point(i, j, 0));
						// 하나의 숫자에 두개의 카드가 존재하니까 LIST배열에는 같은 숫자에 두개의 Point가 들어갈 것
					}
				}
			}
			// 여기까지가 삭제할 카드를 찾아서 ARRAYLIST에 추가한 것
			//예를 들어 1번 카드를 삭제한다면, for문 2개로 여기까지 1번 카드의 위치를 찾은 것이 현재 위치
			// 아직 첫번째 FOR문 안에있음
			if (card.isEmpty())
				continue;
			// 보드에 제거할 숫자가 없을 경우 다음 숫자로 넘어감-->밑에 있는 코드들을 실행,
			// 안시키겠다는 의미

			// 이제 여기 밑의 코드가 실행되는 경우는 우리가 찾으려는 카드가 존재한다는 뜻

			int one = bfs(src, card.get(0)) + bfs(card.get(0), card.get(1)) + 2;
			// 현재의 src위치부터 0번째에 위치해 있는 카드까지의 이동횟수(위에 ArrayList로 선언된 card배열에 저장되어있음)
			// +0번에 위치해있는(왜? 0번까지 이동했으니까) 카드의 위치 + 두번째까지의 카드위치를 더함
			// +2인 이유는 2개의 카드를 뒤집으니까(ENTER 각 카드당 한개씩)

			// 이게 역순으로 제거하는 즉 나중에 있는 카드를 제거하고 먼저 있는 카드 제거하는 횟수
			// card배열에는 위에서부터 시작해서 아래로 사라지는 순서로 저장되어있으므로
			int two = bfs(src, card.get(1)) + bfs(card.get(1), card.get(0)) + 2;
			//여기까지는 뒤집은 것이 아니라 해당 카드까지의 뒤집기까지의 횟수를 각각 계산한 것
			// 두개 뒤집었으니 이제 해당 카드들은 없애야함
			for (int i = 0; i < 2; i++) {
				Board[card.get(i).row][card.get(i).col] = 0;
			}
			
			ret = Math.min(ret, one + permutate(card.get(1)));// 순차로 지운다고 가정했을 때, 이제 0의 위치로 이동해서 다시 나머지 카드들의 제거 값 계산해야함
			ret = Math.min(ret, two + permutate(card.get(0)));// 역순으로 지운다고 가정했
			
			for (int i = 0; i < 2; i++) {
				Board[card.get(i).row][card.get(i).col] = num;// 다시 해당 위치 0으로 바꿨던 것에서 복원(위에서 0으로 만들었으니
				// 이후 재귀에서 제대로 된 탐색이 될 수 있도록
				// 즉 예를 들어 첫번째 for문에서 1을 제거한 상태이니까 그 다음 재귀는 2번부터
				// 삭제하는 것, 이 때 1번은 정상적인 상태여야 하므로
			}
		}
		if (ret == INF)// 계속해서 permutate가 호출되서 Card배열에 아무것도 남아있지 않는 상태
			// 가 있을 수 있음 이때에는 계속 continue로 skip될 것이기 때문에
			// ret값이 INF로 유지될 것임
			// 여기까지 그대로 넘어왔다는 것은 Card리스트에 아무것도 담겨있지 않다는 뜻 즉 더이상 제거할 카드가 없다라는 뜻
			// 그렇기 때문에 조작이 필요없다는 뜻이기에 0을 return해준다. ret이 조작횟수이므로
			return 0;
		return ret;
	}

	public int CardSolve(int[][] board, int r, int c) {
		Board = board;
		return permutate(new Point(r, c, 0));// 순열, 0인 이유는 처음에는 조작횟수가 0 이 Point
		// Point 객체가 내가 움직이는 좌표가 되는것(?)
	}

	int[][] randomarraygenerator() {
		int[][] array = new int[4][4];
		Random random = new Random();
		int time = random.nextInt(6) + 1;
		int[] random_array = new int[time];

		for (int i = 0; i < time; i++) {
			random_array[i] = random.nextInt(6) + 1;

			// 중복 확인
			for (int j = 0; j < i; j++) {
				while (random_array[j] == random_array[i]) {
					random_array[i] = random.nextInt(6) + 1;
					j = 0; // 처음부터 다시 검사
				}
			}
		}

		// 결과 출력
		System.out.println("Random Array: " + Arrays.toString(random_array));

		for (int i = 0; i < time; i++) {
            int row = random.nextInt(4);
            int col = random.nextInt(4);
            do {
                row = random.nextInt(4);
                col = random.nextInt(4);
            } while (array[row][col]!=0);
            array[row][col] = random_array[i];
            System.out.println("ROW:\"" + row + "\"COL:\"" + col+"\"" + "ENTERD:" + random_array[i]);
            int row2, col2;
            do {
                row2 = random.nextInt(4);
                col2 = random.nextInt(4);
            } while ((row2 == row && col2 == col) || (array[row2][col2]!=0));
            array[row2][col2] = random_array[i];
            System.out.println("ROW2:\"" + row2 + "\"COL2:\"" + col2+"\""+"ENTERD:" + random_array[i]);
        }

        return array;
    }

	int randomnumbergenerator() {
		return new Random().nextInt(4);
	}

	public static void main(String[] args) {
//		int[][] input_board = { { 4, 1, 0, 2 }, { 4, 1, 0, 5 }, { 5, 0, 0, 3 }, { 2, 0, 0, 3 } };
//		int r = 0;
//		int c = 1;
		CardSolve solve = new CardSolve();
		int[][] input_board = solve.randomarraygenerator();
		int r = solve.randomnumbergenerator();
		int c = solve.randomnumbergenerator();
		for (int i = 0; i < input_board.length; i++) {
			for (int j = 0; j < input_board[i].length; j++) {
				System.out.print(input_board[i][j] + " ");
			}
			System.out.println();
		}
//		input examples...

//		int[][] input_board = new int[4][4];	
//		Scanner sc = new Scanner(System.in);
//		System.out.println("ENTER THE BOARD");
//		for(int i = 0 ; i< 4 ; i++) {
//			for(int j = 0 ; j < 4 ; j++) {
//				input_board[i][j] = sc.nextInt();
//			}
//		}
//		System.out.println("Enter R, C");
//		int r = sc.nextInt();
//		int c = sc.nextInt();

		int result = solve.CardSolve(input_board, r, c);
		System.out.println(result);

		CardGame game = new CardGame();
		game.setBoardValues(input_board);
		game.startlocation(r, c);
		game.resetVal(result);
	}
}
