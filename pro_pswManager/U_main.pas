unit U_main;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, ExtCtrls, Menus, DB, ADODB, ComCtrls, StdCtrls, Grids, DBGrids,shellapi;

type
  Tfrm_main = class(TForm)
    Panel1: TPanel;
    qry1: TADOQuery;
    mm1: TMainMenu;
    x1: TMenuItem;
    N1: TMenuItem;
    lv_main: TListView;
    stat1: TStatusBar;
    spl1: TSplitter;
    Panel2: TPanel;
    DBGrid1: TDBGrid;
    ds1: TDataSource;
    btn1: TButton;
    pm1: TPopupMenu;
    N2: TMenuItem;
    N3: TMenuItem;
    qry_add: TADOQuery;
    N4: TMenuItem;
    N5: TMenuItem;
    url1: TMenuItem;
    tmr1: TTimer;
    tmr2: TTimer;
    procedure btn1Click(Sender: TObject);
    procedure FormShow(Sender: TObject);
    procedure N2Click(Sender: TObject);
    procedure N3Click(Sender: TObject);
    procedure N5Click(Sender: TObject);
    procedure N4Click(Sender: TObject);
    procedure url1Click(Sender: TObject);
    procedure tmr1Timer(Sender: TObject);
    procedure tmr2Timer(Sender: TObject);
  private
    { Private declarations }
     b_show:Boolean;
    lst_value:TList;

    procedure getData;
    
    procedure WriteLog(sLog: String);
  public
    { Public declarations }
  end;
 const
  gsDesKeyOperator = 'KDJDJJS){}1234567890P)($$&&$DFGHJKL';
  SOH = #13#10;
var
  frm_main: Tfrm_main;

implementation

uses U_dm, U_detail, UnitDesEncrypt;

{$R *.dfm}

procedure Tfrm_main.btn1Click(Sender: TObject);
var
  ssql:string;
begin
        with qry1 do
        begin

          Close;
           SQL.Clear;
          SQL.Add('select * from t_user where  1=1');

          Open;



        end;

end;

procedure Tfrm_main.FormShow(Sender: TObject);

begin
  b_show:=false;
       with  lv_main do
       begin
        //      增加表头
         Clear;
         Columns.clear;
         Columns.Add;
         Columns.Add;
         Columns.Add;
         Columns.Add;
         Columns.Add;
         Columns.Add;
         
         Columns.Items[0].Caption:='序号';
         Columns.Items[1].Caption:='名称';
         Columns.Items[2].Caption:='账户';
         Columns.Items[3].Caption:='密码';
         Columns.Items[4].Caption:='描述';
         Columns.Items[5].Caption:='url';
         Columns.Items[0].Width:=50;
         Columns.Items[1].Width:=50;
         Columns.Items[2].Width:=100;
         Columns.Items[3].Width:=100;
         Columns.Items[4].Width:=200;
          Columns.Items[5].Width:=260;
        ViewStyle:=vsreport;
        GridLines:=true;
         Items.Clear;
       end;
       // 读取数据库数据

        getData;


end;

procedure Tfrm_main.getData;
var
  Titem:Tlistitem;
begin

lv_main.Items.Clear;
       with qry1 do
    begin
       Close;
       SQL.Clear;
       SQL.Add('select * from t_pkmsg where 1=1');
       Open;
      while not Eof do
      begin
        Titem:=lv_main.Items.Add;
        Titem.Caption:=fieldbyname('pk_id').Value;
        Titem.SubItems.Add(fieldbyname('pk_name').Value);
        Titem.SubItems.Add(fieldbyname('pk_usr').Value);
       if b_show then
        Titem.SubItems.Add(DecryStrHex(fieldbyname('pk_value').Value,gsDesKeyOperator))
        else
           Titem.SubItems.Add('********');
        Titem.SubItems.Add(fieldbyname('pk_describe').Value);
        Titem.SubItems.Add(fieldbyname('pk_url').Value);
        Next;
      end;



    end ;
end;

procedure Tfrm_main.N2Click(Sender: TObject);
begin
//
     frm_detail:=Tfrm_detail.Create(Self);
     frm_detail.Show;
     getData;

end;


procedure Tfrm_main.WriteLog(sLog: String);
var
  sFileName: String;
  strList: TStringList;
  str: String;
  sr: TSearchRec;
begin
  strList := TStringList.Create;
  try
    sFileName := ExtractFilePath(Application.ExeName) + 'PswManager' + FormatDateTime('YYYYMMDD', now ) + '.log';
    str :=  sLog + SOH;
    if (FindFirst(sFileName,faArchive, sr) = 0) then
      strlist.LoadFromFile(sFileName);
    strList.Text := strList.Text + str;
    strlist.SaveToFile(sFileName);
  finally
    strList.Free;
  end;
end;

procedure Tfrm_main.N3Click(Sender: TObject);
begin

    if  Assigned(lv_main.Selected) then
    begin
         frm_detail:=Tfrm_detail.Create(Self);
         
         frm_detail.sXh:= lv_main.Selected.Caption;
         frm_detail.Show;
      
    end;
end;

procedure Tfrm_main.N5Click(Sender: TObject);
begin
   getData;
end;

procedure Tfrm_main.N4Click(Sender: TObject);
begin
     b_show:=not b_show;

     getData;


end;

procedure Tfrm_main.url1Click(Sender: TObject);
begin
    if  Assigned(lv_main.Selected) then
          ShellExecute(Application.Handle, nil, PChar(lv_main.Selected.SubItems[4]), nil, nil, SW_SHOWNORMAL);
end;

procedure Tfrm_main.tmr1Timer(Sender: TObject);
begin
     stat1.Panels.Items[2].Text:='当前时间：'+formatdatetime('yyyy-mm-dd hh:mm:ss',now);
end;

procedure Tfrm_main.tmr2Timer(Sender: TObject);
begin
    getData;
end;

end.
