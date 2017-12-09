import { Component, OnInit } from '@angular/core';
import { MediaService } from "../../services/media/media.service";
import { AuthentificationService } from "../../services/authentification.service";
import { Media } from '../../models/media';
import { FileUploader, FileUploaderOptions } from 'ng2-file-upload';

const URL = 'http://localhost:8080/medias';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  
})
export class HomeComponent implements OnInit {
  medias: Media[] = [];
  url: string;
  authToken: string;

  constructor(private mediaService : MediaService, private authService : AuthentificationService) {
    console.log(this.url);
    console.log(this.authToken);

  }

  public uploader;
  options: FileUploaderOptions = {
    url: this.authService.server + '/medias',
    authToken: 'Token '+this.authService.token,
    authTokenHeader: 'Authorization',
};
  public hasBaseDropZoneOver:boolean = false;
  public hasAnotherDropZoneOver:boolean = false;

  public fileOverBase(e:any):void {
    this.hasBaseDropZoneOver = e;
  }

  public fileOverAnother(e:any):void {
    this.hasAnotherDropZoneOver = e;
  }

  ngOnInit() {
    console.log(this.options);
    this.uploader = new FileUploader(this.options);
    this.mediaService.getAllMedias().subscribe(medias => {
      console.log(medias);
      this.medias = medias;
  }); 

  }

  ngAfterViewChecked() {
    //$('.materialboxed').materialbox();
}

}
