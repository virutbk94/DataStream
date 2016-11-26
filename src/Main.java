import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

	public static final String INPUT_FILE_NAME = "/home/virutbk/HW4-q4/words_stream.txt";
	public static final String OUT_FILE_NAME = "result/my_counts.txt";
	public static final String HASH_PARAM_FILE_NAME = "/home/virutbk/HW4-q4/hash_params.txt";
	public static final String COUNT_FILE_NAME = "/home/virutbk/HW4-q4/counts.txt";
	public static int n_buckets, p, n_hashs;
	public static int[] a, b;
	public static long x;
	public static int[][] c;

	public static void main(String[] args) throws IOException {
		n_buckets = 10000;
		n_hashs = 5;
		p = 123457;
		a = new int[n_hashs];
		b = new int[n_hashs];
		c = new int[n_hashs][n_buckets];
		loadHashParam();

		String line;
		BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_NAME));
		while ((line = br.readLine()) != null) {
			x = Long.parseLong(line);
			for (int i = 0; i < 5; i++) {
				int j = hash_function(a[i], b[i], p, n_buckets, x);
				c[i][j] += 1;
			}
		}
		br.close();

		BufferedWriter bw = new BufferedWriter(new FileWriter(OUT_FILE_NAME));

		BufferedReader br1 = new BufferedReader(new FileReader(COUNT_FILE_NAME));
		int n, exactly_count, predict_count;
		List<Integer> list = new ArrayList<Integer>();
		
		while ((line = br1.readLine()) != null) {
			String[] data = line.split("\t");
			n = Integer.parseInt(data[0]);
			exactly_count = Integer.parseInt(data[1]);
			for (int i = 0; i < 5; i++) {
				int j = hash_function(a[i], b[i], p, n_buckets, n);
				list.add(c[i][j]);
			}
			predict_count = Collections.min(list);
			bw.write(n + "\t" + predict_count + "\t" + log_log_eror(exactly_count, predict_count));
			bw.newLine();
			list.clear();
		}
		br1.close();
		bw.close();
	}

	private static void loadHashParam() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(HASH_PARAM_FILE_NAME));
			for (int i = 0; i < 5; i++) {
				String line = br.readLine();
				String[] data = line.split("\t");
				a[i] = Integer.parseInt(data[0]);
				b[i] = Integer.parseInt(data[1]);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static int hash_function(int a, int b, int p, int n_buckets, long x) {
		int y = (int) (x % p);
		int hash_value = a * y + b;
		return hash_value % n_buckets;
	}

	public static double log_log_eror(int exactly_count,int predict_count){
		return Math.log(Math.log(Math.abs(predict_count-exactly_count)));
	}
	
}
