unit U_dm;

interface

uses
  SysUtils, Classes, DB, ADODB;

type
  TDM = class(TDataModule)
    con1: TADOConnection;
    procedure DataModuleCreate(Sender: TObject);
  private
    { Private declarations }
  public
    { Public declarations }
  end;

var
  DM: TDM;

implementation

{$R *.dfm}

procedure TDM.DataModuleCreate(Sender: TObject);
var
  str_conn:string;
begin

       try
        str_conn:='Provider=MSDASQL.1;Password=root;Persist Security Info=True;User ID=root;Data Source=mysqlOdbc';
        con1.Close;
        con1.ConnectionString:=str_conn;
        con1.LoginPrompt:=false;
        con1.Connected   :=True;



       except

       end;  

//
end;

end.
