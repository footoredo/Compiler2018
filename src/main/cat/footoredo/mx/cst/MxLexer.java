package cat.footoredo.mx.cst;
// Generated from Mx.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MxLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, T__37=38, 
		T__38=39, T__39=40, T__40=41, T__41=42, T__42=43, T__43=44, IntLiteral=45, 
		BoolLiteral=46, StringLiteral=47, Identifier=48, WS=49, COMMENT=50, LINE_COMMENT=51;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
		"T__17", "T__18", "T__19", "T__20", "T__21", "T__22", "T__23", "T__24", 
		"T__25", "T__26", "T__27", "T__28", "T__29", "T__30", "T__31", "T__32", 
		"T__33", "T__34", "T__35", "T__36", "T__37", "T__38", "T__39", "T__40", 
		"T__41", "T__42", "T__43", "IntLiteral", "Digits", "BoolLiteral", "StringLiteral", 
		"StringCharacters", "StringCharacter", "EscapeSequence", "Identifier", 
		"Letter", "LetterOrDigit", "WS", "COMMENT", "LINE_COMMENT"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "';'", "'.'", "'['", "']'", "'('", "')'", "'new'", "'++'", "'--'", 
		"'+'", "'-'", "'~'", "'!'", "'*'", "'/'", "'%'", "'<='", "'>='", "'>'", 
		"'<'", "'=='", "'!='", "'&'", "'^'", "'|'", "'&&'", "'||'", "'='", "','", 
		"'null'", "'bool'", "'int'", "'string'", "'class'", "'{'", "'}'", "'void'", 
		"'if'", "'else'", "'for'", "'while'", "'return'", "'break'", "'continue'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, "IntLiteral", "BoolLiteral", 
		"StringLiteral", "Identifier", "WS", "COMMENT", "LINE_COMMENT"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public MxLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Mx.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\65\u0154\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64"+
		"\t\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\3\2\3\2\3\3\3\3"+
		"\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\n\3\n\3"+
		"\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3"+
		"\22\3\22\3\22\3\23\3\23\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\26\3\27\3"+
		"\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3\33\3\33\3\34\3\34\3\34\3"+
		"\35\3\35\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3 \3 \3 \3 \3 \3!\3!\3!\3"+
		"!\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3#\3$\3$\3%\3%\3&\3&\3&\3"+
		"&\3&\3\'\3\'\3\'\3(\3(\3(\3(\3(\3)\3)\3)\3)\3*\3*\3*\3*\3*\3*\3+\3+\3"+
		"+\3+\3+\3+\3+\3,\3,\3,\3,\3,\3,\3-\3-\3-\3-\3-\3-\3-\3-\3-\3.\3.\3/\6"+
		"/\u0109\n/\r/\16/\u010a\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\5"+
		"\60\u0116\n\60\3\61\3\61\5\61\u011a\n\61\3\61\3\61\3\62\6\62\u011f\n\62"+
		"\r\62\16\62\u0120\3\63\3\63\5\63\u0125\n\63\3\64\3\64\3\64\3\65\3\65\7"+
		"\65\u012c\n\65\f\65\16\65\u012f\13\65\3\66\3\66\3\67\3\67\38\68\u0136"+
		"\n8\r8\168\u0137\38\38\39\39\39\39\79\u0140\n9\f9\169\u0143\139\39\39"+
		"\39\39\39\3:\3:\3:\3:\7:\u014e\n:\f:\16:\u0151\13:\3:\3:\3\u0141\2;\3"+
		"\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37"+
		"\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37="+
		" ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\2_\60a\61c\2e\2g\2i\62k\2m\2o\63q\64"+
		"s\65\3\2\t\3\2\62;\4\2$$^^\n\2$$))^^ddhhppttvv\5\2C\\aac|\6\2\62;C\\a"+
		"ac|\5\2\13\f\17\17\"\"\4\2\f\f\17\17\2\u0156\2\3\3\2\2\2\2\5\3\2\2\2\2"+
		"\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2"+
		"\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2"+
		"\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2"+
		"\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2"+
		"\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2"+
		"\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2"+
		"M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3"+
		"\2\2\2\2[\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2i\3\2\2\2\2o\3\2\2\2\2q\3\2\2"+
		"\2\2s\3\2\2\2\3u\3\2\2\2\5w\3\2\2\2\7y\3\2\2\2\t{\3\2\2\2\13}\3\2\2\2"+
		"\r\177\3\2\2\2\17\u0081\3\2\2\2\21\u0085\3\2\2\2\23\u0088\3\2\2\2\25\u008b"+
		"\3\2\2\2\27\u008d\3\2\2\2\31\u008f\3\2\2\2\33\u0091\3\2\2\2\35\u0093\3"+
		"\2\2\2\37\u0095\3\2\2\2!\u0097\3\2\2\2#\u0099\3\2\2\2%\u009c\3\2\2\2\'"+
		"\u009f\3\2\2\2)\u00a1\3\2\2\2+\u00a3\3\2\2\2-\u00a6\3\2\2\2/\u00a9\3\2"+
		"\2\2\61\u00ab\3\2\2\2\63\u00ad\3\2\2\2\65\u00af\3\2\2\2\67\u00b2\3\2\2"+
		"\29\u00b5\3\2\2\2;\u00b7\3\2\2\2=\u00b9\3\2\2\2?\u00be\3\2\2\2A\u00c3"+
		"\3\2\2\2C\u00c7\3\2\2\2E\u00ce\3\2\2\2G\u00d4\3\2\2\2I\u00d6\3\2\2\2K"+
		"\u00d8\3\2\2\2M\u00dd\3\2\2\2O\u00e0\3\2\2\2Q\u00e5\3\2\2\2S\u00e9\3\2"+
		"\2\2U\u00ef\3\2\2\2W\u00f6\3\2\2\2Y\u00fc\3\2\2\2[\u0105\3\2\2\2]\u0108"+
		"\3\2\2\2_\u0115\3\2\2\2a\u0117\3\2\2\2c\u011e\3\2\2\2e\u0124\3\2\2\2g"+
		"\u0126\3\2\2\2i\u0129\3\2\2\2k\u0130\3\2\2\2m\u0132\3\2\2\2o\u0135\3\2"+
		"\2\2q\u013b\3\2\2\2s\u0149\3\2\2\2uv\7=\2\2v\4\3\2\2\2wx\7\60\2\2x\6\3"+
		"\2\2\2yz\7]\2\2z\b\3\2\2\2{|\7_\2\2|\n\3\2\2\2}~\7*\2\2~\f\3\2\2\2\177"+
		"\u0080\7+\2\2\u0080\16\3\2\2\2\u0081\u0082\7p\2\2\u0082\u0083\7g\2\2\u0083"+
		"\u0084\7y\2\2\u0084\20\3\2\2\2\u0085\u0086\7-\2\2\u0086\u0087\7-\2\2\u0087"+
		"\22\3\2\2\2\u0088\u0089\7/\2\2\u0089\u008a\7/\2\2\u008a\24\3\2\2\2\u008b"+
		"\u008c\7-\2\2\u008c\26\3\2\2\2\u008d\u008e\7/\2\2\u008e\30\3\2\2\2\u008f"+
		"\u0090\7\u0080\2\2\u0090\32\3\2\2\2\u0091\u0092\7#\2\2\u0092\34\3\2\2"+
		"\2\u0093\u0094\7,\2\2\u0094\36\3\2\2\2\u0095\u0096\7\61\2\2\u0096 \3\2"+
		"\2\2\u0097\u0098\7\'\2\2\u0098\"\3\2\2\2\u0099\u009a\7>\2\2\u009a\u009b"+
		"\7?\2\2\u009b$\3\2\2\2\u009c\u009d\7@\2\2\u009d\u009e\7?\2\2\u009e&\3"+
		"\2\2\2\u009f\u00a0\7@\2\2\u00a0(\3\2\2\2\u00a1\u00a2\7>\2\2\u00a2*\3\2"+
		"\2\2\u00a3\u00a4\7?\2\2\u00a4\u00a5\7?\2\2\u00a5,\3\2\2\2\u00a6\u00a7"+
		"\7#\2\2\u00a7\u00a8\7?\2\2\u00a8.\3\2\2\2\u00a9\u00aa\7(\2\2\u00aa\60"+
		"\3\2\2\2\u00ab\u00ac\7`\2\2\u00ac\62\3\2\2\2\u00ad\u00ae\7~\2\2\u00ae"+
		"\64\3\2\2\2\u00af\u00b0\7(\2\2\u00b0\u00b1\7(\2\2\u00b1\66\3\2\2\2\u00b2"+
		"\u00b3\7~\2\2\u00b3\u00b4\7~\2\2\u00b48\3\2\2\2\u00b5\u00b6\7?\2\2\u00b6"+
		":\3\2\2\2\u00b7\u00b8\7.\2\2\u00b8<\3\2\2\2\u00b9\u00ba\7p\2\2\u00ba\u00bb"+
		"\7w\2\2\u00bb\u00bc\7n\2\2\u00bc\u00bd\7n\2\2\u00bd>\3\2\2\2\u00be\u00bf"+
		"\7d\2\2\u00bf\u00c0\7q\2\2\u00c0\u00c1\7q\2\2\u00c1\u00c2\7n\2\2\u00c2"+
		"@\3\2\2\2\u00c3\u00c4\7k\2\2\u00c4\u00c5\7p\2\2\u00c5\u00c6\7v\2\2\u00c6"+
		"B\3\2\2\2\u00c7\u00c8\7u\2\2\u00c8\u00c9\7v\2\2\u00c9\u00ca\7t\2\2\u00ca"+
		"\u00cb\7k\2\2\u00cb\u00cc\7p\2\2\u00cc\u00cd\7i\2\2\u00cdD\3\2\2\2\u00ce"+
		"\u00cf\7e\2\2\u00cf\u00d0\7n\2\2\u00d0\u00d1\7c\2\2\u00d1\u00d2\7u\2\2"+
		"\u00d2\u00d3\7u\2\2\u00d3F\3\2\2\2\u00d4\u00d5\7}\2\2\u00d5H\3\2\2\2\u00d6"+
		"\u00d7\7\177\2\2\u00d7J\3\2\2\2\u00d8\u00d9\7x\2\2\u00d9\u00da\7q\2\2"+
		"\u00da\u00db\7k\2\2\u00db\u00dc\7f\2\2\u00dcL\3\2\2\2\u00dd\u00de\7k\2"+
		"\2\u00de\u00df\7h\2\2\u00dfN\3\2\2\2\u00e0\u00e1\7g\2\2\u00e1\u00e2\7"+
		"n\2\2\u00e2\u00e3\7u\2\2\u00e3\u00e4\7g\2\2\u00e4P\3\2\2\2\u00e5\u00e6"+
		"\7h\2\2\u00e6\u00e7\7q\2\2\u00e7\u00e8\7t\2\2\u00e8R\3\2\2\2\u00e9\u00ea"+
		"\7y\2\2\u00ea\u00eb\7j\2\2\u00eb\u00ec\7k\2\2\u00ec\u00ed\7n\2\2\u00ed"+
		"\u00ee\7g\2\2\u00eeT\3\2\2\2\u00ef\u00f0\7t\2\2\u00f0\u00f1\7g\2\2\u00f1"+
		"\u00f2\7v\2\2\u00f2\u00f3\7w\2\2\u00f3\u00f4\7t\2\2\u00f4\u00f5\7p\2\2"+
		"\u00f5V\3\2\2\2\u00f6\u00f7\7d\2\2\u00f7\u00f8\7t\2\2\u00f8\u00f9\7g\2"+
		"\2\u00f9\u00fa\7c\2\2\u00fa\u00fb\7m\2\2\u00fbX\3\2\2\2\u00fc\u00fd\7"+
		"e\2\2\u00fd\u00fe\7q\2\2\u00fe\u00ff\7p\2\2\u00ff\u0100\7v\2\2\u0100\u0101"+
		"\7k\2\2\u0101\u0102\7p\2\2\u0102\u0103\7w\2\2\u0103\u0104\7g\2\2\u0104"+
		"Z\3\2\2\2\u0105\u0106\5]/\2\u0106\\\3\2\2\2\u0107\u0109\t\2\2\2\u0108"+
		"\u0107\3\2\2\2\u0109\u010a\3\2\2\2\u010a\u0108\3\2\2\2\u010a\u010b\3\2"+
		"\2\2\u010b^\3\2\2\2\u010c\u010d\7v\2\2\u010d\u010e\7t\2\2\u010e\u010f"+
		"\7w\2\2\u010f\u0116\7g\2\2\u0110\u0111\7h\2\2\u0111\u0112\7c\2\2\u0112"+
		"\u0113\7n\2\2\u0113\u0114\7u\2\2\u0114\u0116\7g\2\2\u0115\u010c\3\2\2"+
		"\2\u0115\u0110\3\2\2\2\u0116`\3\2\2\2\u0117\u0119\7$\2\2\u0118\u011a\5"+
		"c\62\2\u0119\u0118\3\2\2\2\u0119\u011a\3\2\2\2\u011a\u011b\3\2\2\2\u011b"+
		"\u011c\7$\2\2\u011cb\3\2\2\2\u011d\u011f\5e\63\2\u011e\u011d\3\2\2\2\u011f"+
		"\u0120\3\2\2\2\u0120\u011e\3\2\2\2\u0120\u0121\3\2\2\2\u0121d\3\2\2\2"+
		"\u0122\u0125\n\3\2\2\u0123\u0125\5g\64\2\u0124\u0122\3\2\2\2\u0124\u0123"+
		"\3\2\2\2\u0125f\3\2\2\2\u0126\u0127\7^\2\2\u0127\u0128\t\4\2\2\u0128h"+
		"\3\2\2\2\u0129\u012d\5k\66\2\u012a\u012c\5m\67\2\u012b\u012a\3\2\2\2\u012c"+
		"\u012f\3\2\2\2\u012d\u012b\3\2\2\2\u012d\u012e\3\2\2\2\u012ej\3\2\2\2"+
		"\u012f\u012d\3\2\2\2\u0130\u0131\t\5\2\2\u0131l\3\2\2\2\u0132\u0133\t"+
		"\6\2\2\u0133n\3\2\2\2\u0134\u0136\t\7\2\2\u0135\u0134\3\2\2\2\u0136\u0137"+
		"\3\2\2\2\u0137\u0135\3\2\2\2\u0137\u0138\3\2\2\2\u0138\u0139\3\2\2\2\u0139"+
		"\u013a\b8\2\2\u013ap\3\2\2\2\u013b\u013c\7\61\2\2\u013c\u013d\7,\2\2\u013d"+
		"\u0141\3\2\2\2\u013e\u0140\13\2\2\2\u013f\u013e\3\2\2\2\u0140\u0143\3"+
		"\2\2\2\u0141\u0142\3\2\2\2\u0141\u013f\3\2\2\2\u0142\u0144\3\2\2\2\u0143"+
		"\u0141\3\2\2\2\u0144\u0145\7,\2\2\u0145\u0146\7\61\2\2\u0146\u0147\3\2"+
		"\2\2\u0147\u0148\b9\2\2\u0148r\3\2\2\2\u0149\u014a\7\61\2\2\u014a\u014b"+
		"\7\61\2\2\u014b\u014f\3\2\2\2\u014c\u014e\n\b\2\2\u014d\u014c\3\2\2\2"+
		"\u014e\u0151\3\2\2\2\u014f\u014d\3\2\2\2\u014f\u0150\3\2\2\2\u0150\u0152"+
		"\3\2\2\2\u0151\u014f\3\2\2\2\u0152\u0153\b:\2\2\u0153t\3\2\2\2\f\2\u010a"+
		"\u0115\u0119\u0120\u0124\u012d\u0137\u0141\u014f\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}