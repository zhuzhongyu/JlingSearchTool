package coboo.jling;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class ListIndex {

	private static Directory directory;

	private static void searchIndex(Analyzer analyzer, Directory directory,String fieldName,String queryWord)
			throws IOException, ParseException {
		DirectoryReader ireader = DirectoryReader.open(directory);
		IndexSearcher isearcher = new IndexSearcher(ireader);
		// Parse a simple query that searches for "text":
		QueryParser parser = new QueryParser(Version.LUCENE_CURRENT,
				fieldName, analyzer);
		Query query = parser.parse(queryWord);
		ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
		// assertEquals(1, hits.length);
		// Iterate through the results:
		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = isearcher.doc(hits[i].doc);
			// assertEquals("This is the text to be indexed.",
			// hitDoc.get("fieldname"));
			List<IndexableField> fields = hitDoc.getFields();
			for (IndexableField f : fields) {
				String s = f.stringValue();
				System.out.println(s);
			}

		}
		ireader.close();
		directory.close();
	}

	private static Directory indexDir(Analyzer analyzer) throws IOException {

		IndexWriterConfig config = new IndexWriterConfig(
				Version.LUCENE_CURRENT, analyzer);
		IndexWriter iwriter = new IndexWriter(directory, config);
		Document doc = new Document();
		String text = "This is the text to be indexed..........";
		doc.add(new Field("fieldname", text, TextField.TYPE_STORED));
		iwriter.addDocument(doc);
		iwriter.close();
		return directory;
	}
	private static void listIndex(Directory directory) throws IOException {
		DirectoryReader ireader = DirectoryReader.open(directory);
		//IndexSearcher isearcher = new IndexSearcher(ireader);

		for(int i=0;i<ireader.maxDoc();i++){
			System.out.println("---------------------------"+i+"---------------------------------");
			Document d=ireader.document(i);
			List<IndexableField> fields = d.getFields();
			for(IndexableField f:fields){
				String fieldname=f.name();
				String fstring = f.stringValue();
				
				System.out.println(fieldname+":"+fstring);
			}
		}
		ireader.close();
	}
	public static void main(String[] args) throws IOException, ParseException {
		//System.out.print("starting...");
		directory = FSDirectory.open(new File("d:/tmp/index"));
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
		//Directory directory = indexDir(analyzer);
		// Now search the index:
		//searchIndex(analyzer, directory, "content", "nutch");
		listIndex(directory);
	}



}
