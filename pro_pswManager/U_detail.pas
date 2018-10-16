unit U_detail;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, StdCtrls, DB, ADODB, ExtCtrls, EnJpgGr;

type
  Tfrm_detail = class(TForm)
    Label1: TLabel;
    edt_name: TEdit;
    Label2: TLabel;
    edt_value: TEdit;
    Label3: TLabel;
    mmo_remark: TMemo;
    Label4: TLabel;
    mmo_url: TMemo;
    btn1: TButton;
    Button1: TButton;
    qry_add: TADOQuery;
    Label5: TLabel;
    Edit_usr: TEdit;
    Image1: TImage;
    procedure Button1Click(Sender: TObject);
    procedure btn1Click(Sender: TObject);
    procedure FormShow(Sender: TObject);
  private
    { Private declarations }
  public
    { Public declarations }
  sXh:string;

  end;
  const
  gsDesKeyOperator = 'KDJDJJS){}1234567890P)($$&&$DFGHJKL';
var
  frm_detail: Tfrm_detail;

implementation

uses U_dm, UnitDesEncrypt;

{$R *.dfm}

procedure Tfrm_detail.Button1Click(Sender: TObject);
begin
close;
end;

procedure Tfrm_detail.btn1Click(Sender: TObject);

begin
      if Trim(edt_name.Text)='' then
      begin
        ShowMessage('名称不能为空');
        Exit;
      end;
       if Trim(Edit_usr.Text)='' then
      begin
        ShowMessage('账户不能为空');
        Exit;
      end;
      if Trim(edt_value.Text)='' then
      begin
        ShowMessage('密码不能为空');
        Exit;
      end;


    with qry_add do
    begin
       Close;
       SQL.Clear;
        if Trim(sXh)<>'' then
        begin
                 SQL.Add('select * from t_pkmsg where pk_id='+QuotedStr(Trim(sXh)));
          Open;
          Edit;
          fieldbyname('pk_name').Value:=Trim(edt_name.Text);
          fieldbyname('pk_value').Value:=EncryStrHex(Trim(edt_value.Text),gsDesKeyOperator);
          fieldbyname('pk_describe').Value:=Trim(mmo_remark.Text);
          fieldbyname('pk_url').Value:=Trim(mmo_url.Text);
            fieldbyname('pk_usr').Value:=Trim(Edit_usr.Text);
         Post;
        end else
        begin
            SQL.Add('select * from t_pkmsg where 1=0');
          Open;
        Append;
        fieldbyname('pk_name').Value:=Trim(edt_name.Text);
        fieldbyname('pk_value').Value:=EncryStrHex(Trim(edt_value.Text),gsDesKeyOperator);
        fieldbyname('pk_describe').Value:=Trim(mmo_remark.Text);
        fieldbyname('pk_url').Value:=Trim(mmo_url.Text);
        fieldbyname('pk_usr').Value:=Trim(Edit_usr.Text);
        Post;
        end;


     end;

     Close;
end;

procedure Tfrm_detail.FormShow(Sender: TObject);
begin
    //
//    ShowMessage(sXh);
        if Trim(sXh)<>'' then
            begin
                  with qry_add do
                  begin
                     Close;
                     SQL.Clear;
                     SQL.Add('select * from t_pkmsg where pk_id='+QuotedStr(Trim(sXh)));
                     Open;
                      edt_name.Text:=fieldbyname('pk_name').Value;
                      Edit_usr.Text:=fieldbyname('pk_usr').Value;
                      edt_value.Text:=DecryStrHex(fieldbyname('pk_value').Value,gsDesKeyOperator);
                      mmo_remark.Text:=fieldbyname('pk_describe').Value;
                      mmo_url.Text:=fieldbyname('pk_url').Value;
                  end

            end;



end;

end.
