import { Component, OnInit } from '@angular/core';

import { AuthentificationService } from '../../services/authentification.service';

import { FileUploader, FileUploaderOptions } from 'ng2-file-upload';

const URL = 'http://localhost:8080/medias';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  url: string;
  authToken: string;

  public hasBaseDropZoneOver = false;
  public hasAnotherDropZoneOver = false;
  public uploader;

  options: FileUploaderOptions = {
    url: this.authService.server + '/medias',
    authToken: 'Token ' + this.authService.token,
    authTokenHeader: 'Authorization',
  };

  constructor(private authService: AuthentificationService) {
  }

  ngOnInit() {
    console.log(this.options);
    this.uploader = new FileUploader(this.options);
  }

  public fileOverBase(e: any): void {
    this.hasBaseDropZoneOver = e;
  }

  public fileOverAnother(e: any): void {
    this.hasAnotherDropZoneOver = e;
  }


}
