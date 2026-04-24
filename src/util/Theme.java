package util;

import java.awt.Color;
import java.awt.Font;

/**
 * Konstanta warna dan font yang dipakai di seluruh aplikasi.
 * Semua form / panel HARUS menggunakan konstanta ini agar tampilan konsisten.
 */
public class Theme {

    // ── Primary brand color (ungu Teams-like) ──
    public static final Color PRIMARY        = new Color(0x5B5FC7);
    public static final Color PRIMARY_DARK   = new Color(0x4A4EB8);
    public static final Color PRIMARY_LIGHT  = new Color(0xEEF2FF);

    // ── Backgrounds ──
    public static final Color BG_APP         = new Color(0xF3F4F6); // Abu terang (halaman)
    public static final Color BG_WHITE       = Color.WHITE;
    public static final Color BG_TOPBAR      = PRIMARY;
    public static final Color BG_SIDEBAR     = Color.WHITE;
    public static final Color BG_TABLE_HEADER= new Color(0xF9FAFB);

    // ── Teks ──
    public static final Color TEXT_DARK      = new Color(0x111827);
    public static final Color TEXT_BODY      = new Color(0x374151);
    public static final Color TEXT_MUTED     = new Color(0x6B7280);
    public static final Color TEXT_WHITE     = Color.WHITE;

    // ── Status / badge colors ──
    public static final Color SUCCESS        = new Color(0x16A34A);
    public static final Color SUCCESS_BG     = new Color(0xD1FAE5);
    public static final Color WARNING        = new Color(0xD97706);
    public static final Color WARNING_BG     = new Color(0xFEF3C7);
    public static final Color DANGER         = new Color(0xDC2626);
    public static final Color DANGER_BG      = new Color(0xFEE2E2);
    public static final Color INFO           = new Color(0x2563EB);
    public static final Color INFO_BG        = new Color(0xDBEAFE);

    // ── Borders ──
    public static final Color BORDER         = new Color(0xE5E7EB);
    public static final Color BORDER_LIGHT   = new Color(0xF3F4F6);

    // ── Fonts (Segoe UI tersedia di Windows; fallback SansSerif) ──
    public static final String FONT_NAME     = "Segoe UI";
    public static final Font   FONT_REGULAR  = new Font(FONT_NAME, Font.PLAIN,  13);
    public static final Font   FONT_BOLD     = new Font(FONT_NAME, Font.BOLD,   13);
    public static final Font   FONT_SMALL    = new Font(FONT_NAME, Font.PLAIN,  11);
    public static final Font   FONT_TITLE    = new Font(FONT_NAME, Font.BOLD,   22);
    public static final Font   FONT_SUBTITLE = new Font(FONT_NAME, Font.PLAIN,  13);
    public static final Font   FONT_TOPBAR   = new Font(FONT_NAME, Font.BOLD,   15);
    public static final Font   FONT_MENU     = new Font(FONT_NAME, Font.PLAIN,  13);
    public static final Font   FONT_MENU_ACTIVE = new Font(FONT_NAME, Font.BOLD, 13);

    // ── Spacing ──
    public static final int GAP_XS  = 4;
    public static final int GAP_SM  = 8;
    public static final int GAP_MD  = 12;
    public static final int GAP_LG  = 16;
    public static final int GAP_XL  = 24;
    public static final int GAP_2XL = 32;

    // ── Preferred sizes ──
    public static final int TOPBAR_HEIGHT  = 48;
    public static final int SIDEBAR_WIDTH  = 260;
    public static final int ROW_HEIGHT     = 36;
    public static final int BTN_HEIGHT     = 32;
    public static final int INPUT_HEIGHT   = 32;

    //WARNA UNTUK GDASHBOARD GURU
    public static final Color GURU_SIDEBAR_BG        = new Color(0x1E293B);
    public static final Color GURU_SIDEBAR_ACTIVE    = new Color(0x3B82F6);
    public static final Color GURU_SIDEBAR_HOVER     = new Color(0x334155);
    public static final Color GURU_SIDEBAR_TEXT      = new Color(0xCBD5E1);
    public static final Color GURU_SIDEBAR_SECTION   = new Color(0x64748B);
    public static final Color GURU_TOPBAR_BG         = new Color(0x0F172A);
}
