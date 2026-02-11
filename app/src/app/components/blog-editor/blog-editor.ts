import {
  Component, EventEmitter, forwardRef, Input, OnDestroy, OnInit, Output, signal
} from '@angular/core';
import { ControlValueAccessor, FormsModule, NG_VALUE_ACCESSOR } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';
import { TiptapEditorDirective } from 'ngx-tiptap';
import { Editor } from '@tiptap/core';
import StarterKit from '@tiptap/starter-kit';
import Link from '@tiptap/extension-link';
import Image from '@tiptap/extension-image';
import CodeBlockLowlight from '@tiptap/extension-code-block-lowlight';
import Placeholder from '@tiptap/extension-placeholder';
import { common, createLowlight } from 'lowlight';

const lowlight = createLowlight(common);

@Component({
  selector: 'app-blog-editor',
  imports: [
    FormsModule,
    TiptapEditorDirective,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatTooltipModule,
  ],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => BlogEditor),
    multi: true
  }],
  templateUrl: './blog-editor.html',
  styleUrl: './blog-editor.scss'
})
export class BlogEditor implements ControlValueAccessor, OnInit, OnDestroy {
  @Input() content = '';
  @Output() contentChange = new EventEmitter<string>();

  editor!: Editor;
  isRawMode = signal(false);
  rawHtml = '';

  private onChange: (value: string) => void = () => {};
  private onTouched: () => void = () => {};
  private skipNextUpdate = false;

  ngOnInit(): void {
    this.editor = new Editor({
      extensions: [
        StarterKit.configure({
          codeBlock: false,
        }),
        Link.configure({
          openOnClick: false,
          HTMLAttributes: { rel: 'noopener noreferrer', target: '_blank' },
        }),
        Image,
        CodeBlockLowlight.configure({ lowlight }),
        Placeholder.configure({ placeholder: 'Start writing...' }),
      ],
      content: this.content,
      onUpdate: ({ editor }) => {
        if (this.skipNextUpdate) {
          this.skipNextUpdate = false;
          return;
        }
        const html = editor.getHTML();
        this.onChange(html);
        this.contentChange.emit(html);
      },
      onBlur: () => {
        this.onTouched();
      },
    });
  }

  ngOnDestroy(): void {
    this.editor?.destroy();
  }

  // ControlValueAccessor

  writeValue(value: string): void {
    if (!value) {
      this.editor?.commands.clearContent();
      return;
    }
    if (this.editor) {
      this.skipNextUpdate = true;
      this.editor.commands.setContent(value, { emitUpdate: false });
    } else {
      this.content = value;
    }
  }

  registerOnChange(fn: (value: string) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  // Toolbar actions

  toggleBold(): void {
    this.editor.chain().focus().toggleBold().run();
  }

  toggleItalic(): void {
    this.editor.chain().focus().toggleItalic().run();
  }

  toggleStrike(): void {
    this.editor.chain().focus().toggleStrike().run();
  }

  setHeading(level: 1 | 2 | 3): void {
    this.editor.chain().focus().toggleHeading({ level }).run();
  }

  setParagraph(): void {
    this.editor.chain().focus().setParagraph().run();
  }

  toggleBulletList(): void {
    this.editor.chain().focus().toggleBulletList().run();
  }

  toggleOrderedList(): void {
    this.editor.chain().focus().toggleOrderedList().run();
  }

  toggleBlockquote(): void {
    this.editor.chain().focus().toggleBlockquote().run();
  }

  toggleCodeBlock(): void {
    this.editor.chain().focus().toggleCodeBlock().run();
  }

  setHorizontalRule(): void {
    this.editor.chain().focus().setHorizontalRule().run();
  }

  setLink(): void {
    const previousUrl = this.editor.getAttributes('link')['href'] || '';
    const url = window.prompt('Enter URL:', previousUrl);

    if (url === null) return;

    if (url === '') {
      this.editor.chain().focus().extendMarkRange('link').unsetLink().run();
      return;
    }

    this.editor.chain().focus().extendMarkRange('link').setLink({ href: url }).run();
  }

  insertImage(): void {
    const url = window.prompt('Enter image URL:');
    if (url) {
      this.editor.chain().focus().setImage({ src: url }).run();
    }
  }

  undo(): void {
    this.editor.chain().focus().undo().run();
  }

  redo(): void {
    this.editor.chain().focus().redo().run();
  }

  // Raw HTML toggle

  toggleRawMode(): void {
    if (this.isRawMode()) {
      this.skipNextUpdate = true;
      this.editor.commands.setContent(this.rawHtml, { emitUpdate: false });
      const html = this.editor.getHTML();
      this.onChange(html);
      this.contentChange.emit(html);
    } else {
      this.rawHtml = this.editor.getHTML();
    }
    this.isRawMode.update(v => !v);
  }

  onRawInput(): void {
    this.onChange(this.rawHtml);
    this.contentChange.emit(this.rawHtml);
  }

  // Active state helpers

  isActive(name: string, attrs?: Record<string, unknown>): boolean {
    return this.editor?.isActive(name, attrs) ?? false;
  }
}
