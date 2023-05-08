package org.surus.lang.tools;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Editor extends EditText {
	public static class LanguagesSyntaxHighlightPatterns {
		public static PatternGroup getPatternsForLanguage(String language) {
			switch (language.toLowerCase()) {
			case ("javascript"):
				return PatternGroup.parse(new ColoredPattern("#F08000",
						"\\b(var|let|const|function|break|continue|return|delete|class|export|import|default|for|while|do|if|else|switch|case|this|new|try|catch|finally|yield|await|of)\\b"),
						new ColoredPattern("#CCCCFF", "[0-9]"), new ColoredPattern("#4F7942", "((\".*\")|('.*'))"));
			case ("java_script"):
				return LanguagesSyntaxHighlightPatterns.getPatternsForLanguage("JavaScript");

			case ("java script"):
				return LanguagesSyntaxHighlightPatterns.getPatternsForLanguage("JavaScript");
			case ("html"):

				break;
			}
                        /*case ("any lang"):
                                  return PatternGroup.parse(new ColoredPattern("#color","regex"))
                        */
			return null;
		}
	}

	public static class AsyncRegex extends Async<List<RegexResultIndexs>, TextWithRegion> {
		private Pattern v2;

		public AsyncRegex(Pattern pattern) {
			v2 = pattern;
		}

		public Pattern getPattern() {
			return v2;
		}

		@Override
		public List<RegexResultIndexs> inBackground(TextWithRegion text) {
			List<RegexResultIndexs> result = new ArrayList<>();
			Matcher m = getPattern().matcher(text.text);
			m.region(text.start, text.end);
			while (m.find()) {
				result.add(new RegexResultIndexs(m.start(), m.end()));
			}

			return result;
		}

	}

	public PatternGroup PATTERNS = PatternGroup.parse(new ColoredPattern("#F08000",
			"\\b(var|let|const|function|break|continue|return|delete|class|export|import|default|for|while|do|if|else|switch|case|this|new|try|catch|finally|yield|await|of)\\b"),
			new ColoredPattern("#CCCCFF", "[0-9]"), new ColoredPattern("#4F7942", "((\".*\")|('.*'))"));
	private AsyncRegex ar = null;
	private List<RegexResultIndexs> lastResult;
	private Rect rect;
	private Paint paint;
	private boolean logger = false;

	public Editor(Context context) {
		super(context);
		init();
	}

	public Editor(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public Editor(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();

	}

	private void init() {
		setPadding(6, 6, 6, 6);
		addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (lastResult == null) {
					syntaxHighlight();
				} else {
					try {
						int after = s.length();
						int len = Math.abs(after - before);
						int ab_diffirence = before - after;
						int end = ab_diffirence < 0 ? start + len : start - len;
						start = end <= start ? end : start;

						int highlight_zone_start = start - len < 0 ? 0 : start - len;
						int highlight_zone_end = end;
						syntaxHighlightWithRegion(highlight_zone_start, highlight_zone_end);
					} catch (Exception e) {
						if (isLoggerEnabled()) {
							setText(s + "\n" + e.toString());
						}
					}
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}

		});
		setRawInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		setHorizontallyScrolling(true);
		setVerticalScrollBarEnabled(true);
		setHorizontalScrollBarEnabled(true);
		rect = new Rect();
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.GRAY);
		paint.setTextSize(40);
		paint.setTypeface(Typeface.MONOSPACE);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Layout layout_ = getLayout();
		int position_ = getSelectionStart();
		int line_ = layout_.getLineForOffset(position_);

		int start_Line = layout_.getLineStart(line_);
		int end_Of_Line = layout_.getLineEnd(line_);

		String text_ = getText().toString();

		Paint colorCustom = new Paint();
		// change color of rectangle
		colorCustom.setColor(Color.DKGRAY);
		colorCustom.setStrokeWidth(2);
		colorCustom.setAntiAlias(true);
		colorCustom.setStyle(Paint.Style.STROKE);

		Rect rect_ = new Rect();

		Paint paint_ = getPaint();
		paint_.getTextBounds(text_, start_Line, end_Of_Line, rect_);

		int lineTop_ = layout_.getLineTop(line_) + getPaddingBottom();
		int lineBottom_ = layout_.getLineBottom(line_) + getPaddingTop();

		rect_.top = lineTop_;
		rect_.bottom = lineBottom_;
		rect_.left = 4;
		rect_.right = getContext().getResources().getDisplayMetrics().widthPixels - 4;

		canvas.drawRect(rect_, colorCustom);
		int baseline;
		int lineCount = getLineCount();
		int lineNumber = 1;

		for (int i = 0; i < lineCount; ++i) {
			baseline = getLineBounds(i, null);
			if (i == 0) {
				canvas.drawText("" + lineNumber, rect.left, baseline, paint);
				++lineNumber;
			} else if (getText().charAt(getLayout().getLineStart(i) - 1) == '\n') {
				canvas.drawText("" + lineNumber, rect.left, baseline, paint);
				++lineNumber;
			}
		}

		// for setting edittext start padding
		if (lineCount < 100) {
			setPadding(70, getPaddingTop(), getPaddingRight(), getPaddingBottom());

		}

		else if (lineCount > 100 && lineCount < 1000) {

			setPadding(80, getPaddingTop(), getPaddingRight(), getPaddingBottom());

		} else if (lineCount > 1000 && lineCount < 10000) {

			setPadding(100, getPaddingTop(), getPaddingRight(), getPaddingBottom());

		} else if (lineCount > 10000 && lineCount < 100000) {

			setPadding(120, getPaddingTop(), getPaddingRight(), getPaddingBottom());
		}

	}

	public void enableLogger(boolean b) {
		logger = b;
	}

	public boolean isLoggerEnabled() {
		return logger;
	}

	@Override
	public void onTextChanged(CharSequence newText, int start, int before_len, int after_len) {
		super.onTextChanged(newText, start, before_len, after_len);
	}

	public void syntaxHighlight() {
		syntaxHighlight(getText().toString());
	}

	public void syntaxHighlight(String text) {
		syntaxHighlightWithRegion(text, 0, text.length());
	}

	public void syntaxHighlightWithRegion(String text, int start, int end) {
		for (ColoredPattern cp : PATTERNS.patterns()) {
			ar = new AsyncRegex(cp.pattern);
			List<RegexResultIndexs> r = ar.execute(new TextWithRegion(text, start, end));
			if (r != lastResult) {
				lastResult = r;
				if (r != null) {
					for (RegexResultIndexs result : lastResult) {
						getText().setSpan(new ForegroundColorSpan(cp.color), result.start, result.end,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
			}
		}
	}

	public void syntaxHighlightWithRegion(int start, int end) {
		syntaxHighlightWithRegion(getText().toString(), start, end);
	}

	public void syntaxHighlightFromIndex(int start) {
		syntaxHighlightWithRegion(start, getText().toString().length());
	}

	public void setSyntaxHighlightLanguage(String language) {
		PatternGroup pg = LanguagesSyntaxHighlightPatterns.getPatternsForLanguage(language);
		if (pg != null) {
			PATTERNS = pg;
			syntaxHighlight();
		}
	}
}
