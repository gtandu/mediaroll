export class User {
    mail:String;
    password:String;

    constructor(private model:any){
        this.mail = model.email;
        this.password = model.password;
    }
}
