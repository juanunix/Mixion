/*
 * Copyright (c) 2017-2018 Gregor Santner and Markor contributors
 *
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */
package com.taskail.mixion.markortexteditor;

import android.app.Activity;

import com.taskail.mixion.markortexteditor.converter.MarkdownTextConverter;
import com.taskail.mixion.markortexteditor.converter.TextConverter;
import com.taskail.mixion.markortexteditor.highlighter.Highlighter;
import com.taskail.mixion.markortexteditor.highlighter.MarkdownHighlighter;
import com.taskail.mixion.markortexteditor.moduleactions.MarkdownTextModuleActions;
import com.taskail.mixion.markortexteditor.moduleactions.TextModuleActions;


public class TextFormat {
    public static final int FORMAT_UNKNOWN = 0;
    public static final int FORMAT_MARKDOWN = 1;
//    public static final int FORMAT_PLAIN = R.id.action_format_plaintext;
//    public static final int FORMAT_TODOTXT = R.id.action_format_todotxt;

    public interface TextFormatApplier {
        void applyTextFormat(int textFormatId);
    }

    public static TextFormat getFormat(int formatType, Activity activity) {
        TextFormat format = new TextFormat();
        switch (formatType) {
//            case FORMAT_PLAIN: {
//                format.setConverter(new PlainTextConverter());
//                format.setHighlighter(new PlainHighlighter());
//                format.setTextModuleActions(new PlainTextModuleActions(activity));
//                break;
//            }
//            case FORMAT_TODOTXT: {
//                format.setConverter(new TodoTxtTextConverter());
//                format.setHighlighter(new TodoTxtHighlighter());
//                format.setTextModuleActions(new TodoTxtTextModuleActions(activity, document));
//                break;
//            }
            default:
            case FORMAT_MARKDOWN: {
                format.setConverter(new MarkdownTextConverter());
                format.setHighlighter(new MarkdownHighlighter());
                format.setTextModuleActions(new MarkdownTextModuleActions(activity));
                break;
            }
        }
        return format;
    }

    //
    //
    //
    private TextModuleActions _textModuleActions;
    private Highlighter _highlighter;
    private TextConverter _converter;

    public TextFormat() {
    }

    public TextFormat(TextModuleActions textModuleActions, Highlighter highlighter, MarkdownTextConverter converter) {
        _textModuleActions = textModuleActions;
        _highlighter = highlighter;
        _converter = converter;
    }


    //
    //
    //

    public TextModuleActions getTextModuleActions() {
        return _textModuleActions;
    }

    public void setTextModuleActions(TextModuleActions textModuleActions) {
        _textModuleActions = textModuleActions;
    }

    public Highlighter getHighlighter() {
        return _highlighter;
    }

    public void setHighlighter(Highlighter highlighter) {
        _highlighter = highlighter;
    }

    public TextConverter getConverter() {
        return _converter;
    }

    public void setConverter(TextConverter converter) {
        _converter = converter;
    }
}
