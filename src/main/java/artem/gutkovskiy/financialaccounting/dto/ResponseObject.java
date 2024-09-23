    package artem.gutkovskiy.financialaccounting.dto;
    import java.util.Objects;

    public class ResponseObject {
        private Long id;
        private String message;
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ResponseObject that = (ResponseObject) o;
            return id.equals(that.id) && message.equals(that.message);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, message);
        }


        public ResponseObject(Long id, String message) {
            this.id = id;
            this.message = message;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

