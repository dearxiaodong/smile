program ProPswManager;

uses
  Forms,
  U_main in 'U_main.pas' {frm_main},
  U_dm in 'U_dm.pas' {DM: TDataModule},
  U_commonUtil in 'U_commonUtil.pas',
  U_detail in 'U_detail.pas' {frm_detail},
  UnitDesEncrypt in 'UnitDesEncrypt.pas',
  U_login in 'U_login.pas' {frm_login};

{$R *.res}

begin
  Application.Initialize;
  Application.CreateForm(Tfrm_main, frm_main);
  Application.CreateForm(TDM, DM);
  Application.CreateForm(Tfrm_detail, frm_detail);
  Application.CreateForm(Tfrm_login, frm_login);
  Application.Run;
end.
