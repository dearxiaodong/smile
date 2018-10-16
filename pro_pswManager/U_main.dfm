object frm_main: Tfrm_main
  Left = 439
  Top = 169
  Width = 831
  Height = 471
  Caption = #23494#30721#31649#29702#22120
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  Menu = mm1
  OldCreateOrder = False
  Position = poScreenCenter
  OnShow = FormShow
  PixelsPerInch = 96
  TextHeight = 13
  object Panel1: TPanel
    Left = 0
    Top = 0
    Width = 815
    Height = 413
    Align = alClient
    TabOrder = 0
    object spl1: TSplitter
      Left = 1
      Top = 1
      Width = 5
      Height = 392
      Cursor = crHSplit
      Beveled = True
      Color = clLime
      ParentColor = False
      ResizeStyle = rsNone
    end
    object Panel2: TPanel
      Left = 6
      Top = 1
      Width = 808
      Height = 392
      Align = alClient
      TabOrder = 2
      Visible = False
      object DBGrid1: TDBGrid
        Left = 240
        Top = 256
        Width = 320
        Height = 120
        DataSource = ds1
        TabOrder = 0
        TitleFont.Charset = DEFAULT_CHARSET
        TitleFont.Color = clWindowText
        TitleFont.Height = -11
        TitleFont.Name = 'MS Sans Serif'
        TitleFont.Style = []
      end
      object btn1: TButton
        Left = 160
        Top = 280
        Width = 75
        Height = 25
        Caption = 'btn1'
        TabOrder = 1
        OnClick = btn1Click
      end
    end
    object lv_main: TListView
      Left = 6
      Top = 1
      Width = 808
      Height = 392
      Align = alClient
      Columns = <
        item
        end
        item
        end
        item
        end>
      RowSelect = True
      PopupMenu = pm1
      TabOrder = 0
      ViewStyle = vsReport
    end
    object stat1: TStatusBar
      Left = 1
      Top = 393
      Width = 813
      Height = 19
      Panels = <
        item
          Width = 300
        end
        item
          Width = 200
        end
        item
          Alignment = taRightJustify
          Width = 50
        end>
      SimplePanel = False
    end
  end
  object qry1: TADOQuery
    Connection = DM.con1
    Parameters = <>
    Left = 144
    Top = 296
  end
  object mm1: TMainMenu
    Left = 152
    Top = 16
    object x1: TMenuItem
      Caption = #31995#32479
    end
    object N1: TMenuItem
      Caption = #24110#21161
    end
  end
  object ds1: TDataSource
    DataSet = qry1
    Left = 176
    Top = 296
  end
  object pm1: TPopupMenu
    Left = 400
    Top = 208
    object N2: TMenuItem
      Caption = #26032#22686
      OnClick = N2Click
    end
    object N3: TMenuItem
      Caption = #20462#25913
      OnClick = N3Click
    end
    object N4: TMenuItem
      Caption = #26174#31034'/'#20851#38381#26126#25991
      OnClick = N4Click
    end
    object N5: TMenuItem
      Caption = #33719#21462#26032#25968#25454
      OnClick = N5Click
    end
    object url1: TMenuItem
      Caption = #25171#24320'url'
      OnClick = url1Click
    end
  end
  object qry_add: TADOQuery
    Connection = DM.con1
    Parameters = <>
    Left = 360
    Top = 208
  end
  object tmr1: TTimer
    OnTimer = tmr1Timer
    Left = 672
    Top = 160
  end
  object tmr2: TTimer
    Interval = 60000
    OnTimer = tmr2Timer
    Left = 448
    Top = 296
  end
end
