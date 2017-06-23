object frm_login: Tfrm_login
  Left = 423
  Top = 255
  Width = 423
  Height = 338
  Caption = 'login'
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  OldCreateOrder = False
  PixelsPerInch = 96
  TextHeight = 13
  object Label1: TLabel
    Left = 96
    Top = 99
    Width = 24
    Height = 13
    Caption = #23494#30721
  end
  object Edit1: TEdit
    Left = 160
    Top = 96
    Width = 121
    Height = 21
    PasswordChar = '*'
    TabOrder = 0
  end
  object Button1: TButton
    Left = 208
    Top = 144
    Width = 75
    Height = 25
    Caption = 'login'
    TabOrder = 1
    OnClick = Button1Click
  end
end
